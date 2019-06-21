package pl.draciel.slackify.slack;

import io.reactivex.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.draciel.slackify.domain.AddTrackLog;
import pl.draciel.slackify.domain.RemoveTrackLog;
import pl.draciel.slackify.domain.User;
import pl.draciel.slackify.spotify.SpotifyFacade;
import pl.draciel.slackify.spotify.exceptions.TrackNotFound;
import pl.draciel.slackify.spotify.model.Track;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/slack")
public class SlackController {

    @Nonnull
    private final SpotifyFacade spotifyFacade;

    @Nonnull
    private final SlackFacade slackFacade;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Single<String> addTrack(@RequestBody @Nonnull final SlashCommand command) {
        return Single.fromCallable(() -> parseRequestParameters(command))
                .compose(slackFacade.slackRequestValidator(command))
                .flatMapObservable(spotifyFacade::searchTrack)
                .firstElement()
                .switchIfEmpty(Maybe.error(new TrackNotFound(String.format(Locale.ENGLISH, "\"%1$s\" not found",
                        command.getParameters()))))
                .flatMapSingle(spotifyFacade::addToPlaylist)
                .flatMap(track -> slackFacade.saveAddTrackLog(AddTrackLog.of(command.getUserName(), command.getUserId(),
                        command.getParameters(), track.getSpotifyId(), LocalDateTime.now()))
                        .andThen(slackFacade.saveUser(User.of(command.getUserName(), command.getUserId())))
                        .andThen(Single.just(String.format(Locale.ENGLISH, "%1$s by %2$s has been added to playlist!",
                                track.getName(), track.getArtist()))))
                .compose(errorMessageRetriever());
    }

    @PostMapping(value = "/remove", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Single<String> removeTrack(@RequestBody @Nonnull final SlashCommand command) {
        return Single.fromCallable(() -> parseRequestSongPosition(command))
                .compose(slackFacade.slackRequestValidator(command))
                .flatMap(pos -> spotifyFacade.getTrackByPosition(pos)
                        .flatMapPublisher(track -> slackFacade.findAddTrackLogsBySpotifyId(track.getSpotifyId()))
                        .switchIfEmpty(Flowable.error(new IllegalArgumentException(
                                "Someone is cheating right here, track was added by "
                                        + "SpotifyApp, you can't remove it, contact admin"
                                        + " to force remove it ¯\\_(ツ)_/¯")))
                        .toList()
                        .flatMapObservable(logs -> Observable.fromIterable(logs)
                                .filter(addTrackLog -> addTrackLog.getSlackUserId().equals(command.getUserId()))
                                .switchIfEmpty(slackFacade.findUserBySlackId(logs.get(0).getSlackUserId())
                                        .flatMapObservable(
                                                u -> Observable.<AddTrackLog>error(new IllegalAccessException(
                                                        String.format(Locale.ENGLISH,
                                                                "Track was added by %1$s you can ask %1$s for remove " +
                                                                        "it.",
                                                                u.getUsername()))))))
                        .firstOrError()
                        .flatMapCompletable(log -> spotifyFacade.removeFromPlaylist(pos)
                                .flatMapCompletable(f -> slackFacade.removeAddTrackLog(log)))
                        .andThen(slackFacade.saveRemoveTrackLog(
                                RemoveTrackLog.of(command.getUserName(), command.getUserId(),
                                        command.getParameters(), LocalDateTime.now())))
                        .andThen(Single.just(
                                String.format(Locale.ENGLISH, "Track on position %1$s has been removed.", pos + 1)))
                        .compose(errorMessageRetriever()));
    }

    @PostMapping(value = "/playlist", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected Single<String> playlist(@RequestBody @Nonnull final SlashCommand command) {
        return spotifyFacade.getPlaylistUrl()
                .compose(slackFacade.slackRequestValidator(command))
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
                    return Single.<String>just("Something went wrong, please try again later");
                });
    }

    private static Track parseRequestParameters(@Nonnull final SlashCommand command) {
        final String parameters = command.getParameters();
        final String[] parts = parameters.split("-");

        if (parts.length == 1) {
            throw new IllegalArgumentException("Invalid request format. Please use is Artist - Track");
        }

        if (parts[0].isEmpty()) {
            throw new IllegalArgumentException("Artist can't be empty");
        }

        if (parts[1].isEmpty()) {
            throw new IllegalArgumentException("Track can't be empty");
        }

        return new Track(parts[0].trim(), parts[1].trim(), null);
    }

    private static Integer parseRequestSongPosition(@Nonnull final SlashCommand command) {
        final String parameters = command.getParameters();
        int pos = 0;
        try {
            pos = Integer.parseInt(parameters);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid request format. Please use number.");
        }

        if (pos < 1) {
            throw new IllegalArgumentException("Invalid track position");
        }

        return pos - 1;
    }
}
