package pl.draciel.slackify.spotify;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

interface SpotifyService {

    String BASE_URL = "https://api.spotify.com/";
    String AUTH_URL = "https://accounts.spotify.com/authorize";
    String TOKENS_URL = "https://accounts.spotify.com/api/token";

    @HTTP(method = "DELETE", path = "/v1/users/{user_id}/playlists/{playlist_id}/tracks", hasBody = true)
    Single<RemoveTracksByPositionResponse> removeTracksByPosition(@Path("user_id") String userId,
                                                                  @Path("playlist_id") String playlistId,
                                                                  @Body RemoveTracksByPositionRequest body);

    @GET(value = "/v1/users/{user_id}/playlists/{playlist_id}/")
    Single<GetPlaylistResponse> getPlaylist(@Path("user_id") String userId,
                                            @Path("playlist_id") String playlistId);

    @GET(value = "/v1/users/{user_id}/playlists/{playlist_id}/tracks")
    Single<SpotifyPagingObject<SpotifyPlaylistTrack>> getPlaylistTracks(
            @Path("user_id") String userId, @Path("playlist_id") String playlistId,
            @Query("offset") Integer offset, @Query("limit") Integer limit);

    @POST(value = "/v1/users/{user_id}/playlists/{playlist_id}/tracks")
    Single<AddTrackToPlaylistResponse> addTrackToPlaylist(@Path("user_id") String userId,
                                                          @Path("playlist_id") String playlistId,
                                                          @Query("uris") List<String> uris);

    @GET(value = "/v1/search/")
    Single<SearchTrackResponse> searchTrack(@Query("q") String query,
                                            @Query("type") SpotifySearchQueryTypes type);

    @GET
    @Deprecated
    Single<String> authorize(@Url String url, @Query("client_id") String clientId,
                             @Query("response_type") String responseType,
                             @Query("redirect_uri") String redirectUri, @Nullable @Query("state") String state,
                             @Nullable @Query("scope") String scope,
                             @Nullable @Query("show_dialog") Boolean showDialog);

    @POST
    @FormUrlEncoded
    Single<AuthorizationCodeResponse> grantTokens(@Url String url,
                                                  @Header("Authorization") String basic,
                                                  @Field("grant_type") SpotifyTokenGrantTypes grantTypes,
                                                  @Field("code") String code,
                                                  @Field("redirect_uri") String redirectUri);

    @POST
    @FormUrlEncoded
    Single<AuthorizationCodeResponse> refreshToken(@Url String url,
                                                   @Header("Authorization") String basic,
                                                   @Field("grant_type") SpotifyTokenGrantTypes grantTypes,
                                                   @Field("refresh_token") String refreshToken);

    // player api

    @PUT(value = "/v1/me/player/")
    Completable transferPlayback(@Nonnull @Body TransferPlaybackRequest body);

    @PUT(value = "/v1/me/player/pause")
    Completable pause(@Nullable @Query("device_id") String deviceId);

    @PUT(value = "/v1/me/player/play")
    Completable play(@Nullable @Query("device_id") String deviceId, @Nonnull @Body PlaySongRequest body);

    @PUT(value = "/v1/me/player/play")
    Completable play(@Nullable @Query("device_id") String deviceId);

    @POST(value = "/v1/me/player/previous")
    Completable previous(@Nullable @Query("device_id") String deviceId);

    @POST(value = "/v1/me/player/next")
    Completable next(@Nullable @Query("device_id") String deviceId);

    @PUT(value = "/v1/me/player/volume")
    Completable changeVolume(@Nullable @Query("device_id") String deviceId, @Query("volume_percent") int volume);

    @GET(value = "/v1/me/player/devices")
    Single<GetUserDevicesResponse> getSpotifyDevices();

    //fixme add later currently played track and information about current playback (which includes device etc)


}
