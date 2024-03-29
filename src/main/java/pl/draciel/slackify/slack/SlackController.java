package pl.draciel.slackify.slack;

import io.reactivex.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.draciel.slackify.domain.AddTrackLog;
import pl.draciel.slackify.domain.RemoveTrackLog;
import pl.draciel.slackify.domain.User;
import pl.draciel.slackify.spotify.SpotifyFacade;
import pl.draciel.slackify.spotify.exceptions.TrackNotFound;
import pl.draciel.slackify.spotify.model.Track;
import pl.draciel.slackify.utility.StringUtil;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

import static pl.draciel.slackify.slack.Messages.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/slack")
class SlackController {

    @Nonnull
    private final SpotifyFacade spotifyFacade;

    @Nonnull
    private final SlackFacade slackFacade;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Single<String> addTrack(@RequestBody @Nonnull final SlashCommand command) {
        return slackFacade.validSlashCommand(command)
                .map(SlackController::parseRequestParameters)
                .flatMapObservable(spotifyFacade::searchTrack)
                .firstElement()
                .switchIfEmpty(Maybe.error(new TrackNotFound(TRACK_NOT_FOUND.formatMessage(command.getParameters()))))
                .flatMapSingle(spotifyFacade::addToPlaylist)
                .flatMap(track -> slackFacade.saveAddTrackLog(AddTrackLog.of(command.getUserName(), command.getUserId(),
                        command.getParameters(), track.getSpotifyId(), LocalDateTime.now()))
                        .andThen(slackFacade.saveUser(User.of(command.getUserName(), command.getUserId())))
                        .andThen(Single.just(ADDED_TO_PLAYLIST.formatMessage(track.getName(), track.getArtist()))))
                .compose(errorMessageRetriever());
    }

    @PostMapping(value = "/remove", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Single<String> removeTrack(@RequestBody @Nonnull final SlashCommand command) {
        return slackFacade.validSlashCommand(command)
                .map(SlackController::parseRequestSongPosition)
                .flatMap(pos -> spotifyFacade.getTrackByPosition(pos)
                        .flatMapPublisher(track -> slackFacade.findAddTrackLogsBySpotifyId(track.getSpotifyId()))
                        .switchIfEmpty(Flowable.error(new IllegalArgumentException(REMOVED_ILLEGAL_TRACK.message())))
                        .toList()
                        .flatMapObservable(logs -> Observable.fromIterable(logs)
                                .filter(addTrackLog -> addTrackLog.getSlackUserId().equals(command.getUserId()))
                                .switchIfEmpty(slackFacade.findUserBySlackId(logs.get(0).getSlackUserId())
                                        .flatMapObservable(u -> Observable.error(new IllegalAccessException(
                                                TRACK_WAS_ADDED_BY.formatMessage(u.getUsername()))))))
                        .firstOrError()
                        .flatMapCompletable(log -> spotifyFacade.removeFromPlaylist(pos)
                                .flatMapCompletable(f -> slackFacade.removeAddTrackLog(log)))
                        .andThen(slackFacade.saveRemoveTrackLog(RemoveTrackLog.of(command.getUserName(),
                                command.getUserId(), command.getParameters(), LocalDateTime.now())))
                        .andThen(Single.just(SUCCESFULLY_REMOVED_TRACK.formatMessage(pos + 1))))
                .compose(errorMessageRetriever());
    }

    @PostMapping(value = "/playlist", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Single<String> playlist(@RequestBody @Nonnull final SlashCommand command) {
        return slackFacade.validSlashCommand(command)
                .flatMap(c -> spotifyFacade.getPlaylistUrl())
                .compose(errorMessageRetriever());
    }

    @Nonnull
    private static SingleTransformer<String, String> errorMessageRetriever() {
        return upstream -> upstream
                .onErrorResumeNext(throwable -> {
                    log.error("Error", throwable);
                    if (throwable.getMessage() != null) {
                        return Single.<String>just(throwable.getMessage());
                    }
                    return Single.<String>just(UNKNOWN_MESSAGE.message());
                });
    }

    @Nonnull
    private static Track parseRequestParameters(@Nonnull final SlashCommand command) {
        final String parameters = command.getParameters();
        final String[] parts = parameters.split("-");

        if (parts.length == 1) {
            throw new IllegalArgumentException(USE_ARTIST_TRACK_FORMAT.message());
        }

        if (StringUtil.isNullOrEmpty(parts[0])) {
            throw new IllegalArgumentException(ARTIST_CANT_BE_EMPTY.message());
        }

        if (StringUtil.isNullOrEmpty(parts[1])) {
            throw new IllegalArgumentException(TRACK_CANT_BE_EMPTY.message());
        }

        return new Track(parts[0].trim(), parts[1].trim(), null);
    }

    @Nonnull
    private static Integer parseRequestSongPosition(@Nonnull final SlashCommand command) {
        final String parameters = command.getParameters();
        int pos = 0;
        try {
            pos = Integer.parseInt(parameters);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(USE_NUMBER_FORMAT.message());
        }

        if (pos < 1) {
            throw new IllegalArgumentException(INVALID_TRACK_POSITION.message());
        }

        return pos - 1;
    }
}
