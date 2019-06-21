package pl.draciel.slackify.slack;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.annotations.SchedulerSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.draciel.slackify.Config;
import pl.draciel.slackify.domain.AddTrackLog;
import pl.draciel.slackify.domain.RemoveTrackLog;
import pl.draciel.slackify.domain.User;
import pl.draciel.slackify.slack.exception.UserNotFound;
import pl.draciel.slackify.spotify.exceptions.InvalidCredentialException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import static pl.draciel.slackify.slack.Messages.*;

@Slf4j
@AllArgsConstructor
public class SlackFacade {

    @Nonnull
    private final Config config;

    @Nonnull
    private final AddTrackLogRepository addTrackLogRepository;

    @Nonnull
    private final RemoveTrackLogRepository removeTrackLogRepository;

    @Nonnull
    private final UserRepository userRepository;

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<SlashCommand> validSlashCommand(@Nonnull final SlashCommand command) {
        return Maybe.just(command)
                .flatMap(c -> Maybe.<SlashCommand>fromCompletable(Completable.mergeArray(
                        validSlackCommandToken(c.getSlackToken()),
                        validTeamId(c.getTeamId())))
                        .defaultIfEmpty(c))
                .toSingle();
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Completable saveAddTrackLog(@Nonnull final AddTrackLog addTrackLog) {
        return Completable.fromAction(() -> addTrackLogRepository.save(addTrackLog));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Completable saveRemoveTrackLog(@Nonnull final RemoveTrackLog removeTrackLog) {
        return Completable.fromAction(() -> removeTrackLogRepository.save(removeTrackLog));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Completable saveUser(@Nonnull final User user) {
        return Maybe.defer(() -> {
            final User currentUser = userRepository.findBySlackUserId(user.getSlackUserId());
            if (currentUser != null) {
                return Maybe.empty();
            }
            return Maybe.just(userRepository.save(user));
        }).ignoreElement();
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<User> findUserBySlackId(@Nonnull final String slackId) {
        return Single.defer(() -> {
            final User user = userRepository.findBySlackUserId(slackId);
            if (user == null) {
                return Single.error(new UserNotFound(USER_NOT_FOUND.message()));
            }
            return Single.just(user);
        });
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Completable removeAddTrackLog(@Nonnull final AddTrackLog addTrackLog) {
        return Completable.fromAction(() -> addTrackLogRepository.delete(addTrackLog));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Flowable<AddTrackLog> findAddTrackLogsBySpotifyId(@Nonnull final String spotifyId) {
        return Flowable.defer(() -> Flowable.fromIterable(addTrackLogRepository.findBySpotifyId(spotifyId)));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    private Completable validSlackCommandToken(String token) {
        return Completable.defer(() -> config.getSlackToken().contains(token) ? Completable.complete()
                : Completable.error(new InvalidCredentialException(INVALID_SLACK_TOKEN.message())));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    private Completable validTeamId(String teamId) {
        return Completable.defer(() -> config.getTeamId().equals(teamId) ? Completable.complete()
                : Completable.error(new InvalidCredentialException(INVALID_TEAM_ID.message())));
    }
}
