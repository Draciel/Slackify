package pl.draciel.slackify.spotify;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.SchedulerSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import pl.draciel.slackify.Config;
import pl.draciel.slackify.security.OAuth2Token;
import pl.draciel.slackify.security.OAuth2TokenStore;
import pl.draciel.slackify.spotify.exceptions.TrackNotFound;
import pl.draciel.slackify.spotify.model.Track;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.draciel.slackify.spotify.Messages.*;

@Slf4j
@AllArgsConstructor
public class SpotifyFacade {

    private final Config config;

    //fixme implement dynamic state
    private final String state = "1341d4125575865f689c9e5135ba031fe961300as23fafcd";

    private final SpotifyService service;

    @Spotify
    private final OAuth2TokenStore tokenStore;

    @Nonnull
    private final PlaylistCache playlist = new PlaylistCache();

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Observable<Track> searchTrack(@Nonnull final Track track) {
        log.debug(track.toString());
        return Single.just(track)
                .compose(applyAutomaticAuthorization())
                .flatMap(t -> service.searchTrack(createTrackSearchQuery(t), SpotifySearchQueryTypes.TRACK))
                .map(SearchTrackResponse::getTracks)
                .map(SpotifyPagingObject::getItems)
                .toObservable()
                .concatMap(Observable::fromIterable)
                .map(SpotifyFacade::map);
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<Track> addToPlaylist(@Nonnull final Track track) {
        log.debug(track.toString());
        if (playlist.anyMatches(t -> t.getSpotifyId().equals(track.getSpotifyId()))) {
            return Single.error(new IllegalStateException(ALREADY_ON_PLAYLIST.formatMessage(track.getName())));
        }
        return service.addTrackToPlaylist(config.getUserId(), config.getPlaylistId(),
                Collections.singletonList("spotify:track:" + track.getSpotifyId()))
                .compose(applyAutomaticAuthorization())
                .doOnSuccess(response -> playlist.add(track))
                .map(r -> track);
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<Track> removeFromPlaylist(final int position) {
        log.debug("" + position);
        if (position < 0 || position > playlist.size()) {
            return Single.error(new IllegalArgumentException(INVALID_POSITION.message()));
        }
        return getPlaylistData()
                .compose(applyAutomaticAuthorization())
                .flatMap(data -> service.removeTracksByPosition(config.getUserId(), config.getPlaylistId(),
                        new RemoveTracksByPositionRequest(Collections.singletonList(position), data.getSnapshotId())))
                .map(f -> playlist.remove(position));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<Track> getTrackByPosition(final int position) {
        if (position < 0 || position > playlist.size()) {
            return Single.error(new IllegalArgumentException(INVALID_POSITION.message()));
        }
        return Single.just(playlist.get(position));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<GetPlaylistResponse> getPlaylistData() {
        return service.getPlaylist(config.getUserId(), config.getPlaylistId())
                .compose(applyAutomaticAuthorization());
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<String> getPlaylistUrl() {
        return Single.just(config.getPlaylistUrl());
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<String> grantTokens(String code, String state) {
        if (!state.equals(this.state)) {
            return Single.error(new IllegalArgumentException(INVALID_STATE.message()));
        }
        return service.grantTokens(SpotifyService.TOKENS_URL, encodeHeader(config.getSpotifyClientId(),
                config.getSpotifyClientSecret()), SpotifyTokenGrantTypes.AUTHORIZATION_CODE, code,
                config.getRedirectUri())
                .map(response -> new OAuth2Token(response.getAccessToken(), response.getRefreshToken(),
                        LocalDateTime.now().plusSeconds(response.getExpiresIn())))
                .flatMapCompletable(token -> Completable.fromAction(() -> updateCredentials(token)))
                .andThen(getPlaylistTracks(0, 100))
                .concatMap(response -> Observable.fromIterable(response.getItems()))
                .map(spt -> {
                    final SpotifyTrack spotifyTrack = spt.getTrack();
                    return new Track(spotifyTrack.getArtists().stream()
                            .map(SpotifyArtist::getName)
                            .collect(Collectors.joining(" & ")), spotifyTrack.getName(),
                            spotifyTrack.getId());
                })
                .toList()
                .flatMap(tracks -> Single.defer(() -> {
                    playlist.cleanAndAddAll(tracks);
                    return Single.just(GRANTED_TOKENS.message());
                }));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public Single<String> authorize(@Nonnull final List<String> scopes) {
        final String url = SpotifyService.AUTH_URL + "?" +
                "client_id=" + config.getSpotifyClientId() + "&" +
                "response_type=" + "code" + "&" +
                "redirect_uri=" + config.getRedirectUri() + "&" +
                "state=" + state + "&" +
                "scope=" + String.join(" ", scopes) + "&" +
                "show_dialog=" + false;
        return Single.just(url);
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    private Observable<SpotifyPagingObject<SpotifyPlaylistTrack>> getPlaylistTracks(@Nonnull final Integer offset,
                                                                                    @Nonnull final Integer limit) {
        return service.getPlaylistTracks(config.getUserId(), config.getPlaylistId(), offset, limit)
                .toObservable()
                .concatMap(response -> {
                    if (response.getNext() == null) {
                        return Observable.just(response);
                    }
                    return Observable.just(response)
                            .concatWith(getPlaylistTracks(response.getLimit(), response.getLimit()));
                });
    }

    private void updateCredentials(OAuth2Token token) {
        tokenStore.setOAuthToken(token);
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    private Single<AuthorizationCodeResponse> refreshAccessToken() {
        final OAuth2Token token = tokenStore.getOAuthToken();
        return service.refreshToken(SpotifyService.TOKENS_URL, "Basic " + new String(
                Base64Utils.encode((config.getSpotifyClientId() + ":" + config.getSpotifyClientSecret())
                        .getBytes())), SpotifyTokenGrantTypes.REFRESH_TOKEN, token.getRefreshToken());
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    private Single<Boolean> isAuthorised() {
        if (tokenStore.getOAuthToken() == null) {
            return Single.error(new IllegalStateException(AUTHORIZATION_REQUIRED.message()));
        }
        if (!LocalDateTime.now().isBefore(tokenStore.getOAuthToken().getExpires())) {
            return Single.just(false);
        }
        return Single.just(true);
    }

    @Nonnull
    private <T> SingleTransformer<T, T> applyAutomaticAuthorization() {
        return upstream -> upstream
                .flatMap(t -> isAuthorised().flatMap(is -> is ? Single.just(t) : refreshAccessToken()
                        .map(SpotifyFacade::map)
                        .flatMapCompletable(token -> Completable.fromAction(() -> updateCredentials(token)))
                        .andThen(Single.just(t))));
    }

    @Nonnull
    private static OAuth2Token map(@Nonnull final AuthorizationCodeResponse response) {
        return new OAuth2Token(response.getAccessToken(), response.getRefreshToken(),
                LocalDateTime.now().plusSeconds(Integer.toUnsignedLong(response.getExpiresIn())));
    }

    @Nonnull
    private static String createTrackSearchQuery(@Nonnull final Track track) {
        return "artist:" + track.getArtist().trim() + ' ' + "track:" + track.getName().trim();
    }

    @Nonnull
    private static String encodeHeader(@Nonnull final String clientId, @Nonnull final String clientSecret) {
        return "Basic " + new String(Base64Utils.encode((clientId + ":" + clientSecret).getBytes()));
    }

    @Nonnull
    private static Track map(@Nonnull final SpotifyTrack spotifyTrack) {
        if (spotifyTrack.getId().isEmpty()) {
            throw new TrackNotFound(TRACK_NOT_FOUND.message());
        }
        return new Track(spotifyTrack.getArtists().stream()
                .map(SpotifyArtist::getName)
                .collect(Collectors.joining(", ")), spotifyTrack.getName(), spotifyTrack.getId());
    }
}
