package pl.draciel.slackify.slack;

import io.reactivex.*;
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
    public <T> SingleTransformer<T, T> slackRequestValidator(@Nonnull final SlashCommand command) {
        return upstream -> upstream.flatMapMaybe(t -> Maybe.<T>fromCompletable(
                Completable.mergeArray(validSlackCommandToken(command.getSlackToken()),
                        validTeamId(command.getTeamId()))).defaultIfEmpty(t))
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
                return Single.error(new UserNotFound("User not found!"));
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
                : Completable.error(new InvalidCredentialException("Invalid slack token")));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    private Completable validTeamId(String teamId) {
        return Completable.defer(() -> config.getTeamId().equals(teamId) ? Completable.complete()
                : Completable.error(new InvalidCredentialException("Invalid team id")));
    }
}
