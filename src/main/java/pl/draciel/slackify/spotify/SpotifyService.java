package pl.draciel.slackify.spotify;

import io.reactivex.Single;
import retrofit2.http.*;

import javax.annotation.Nullable;
import java.util.List;

interface SpotifyService {

    String BASE_URL = "https://api.spotify.com/";
    String AUTH_URL = "https://accounts.spotify.com/authorize";
    String TOKENS_URL = "https://accounts.spotify.com/api/token";

    @HTTP(method = "DELETE", path = "/v1/users/{user_id}/playlists/{playlist_id}/tracks", hasBody = true)
    Single<RemoveTracksByPositionResponse> removeTracksByPosition(@Path("user_id") String userId,
            @Path("playlist_id") String playlistId, @Body RemoveTracksByPositionRequest body);

    @GET(value = "/v1/users/{user_id}/playlists/{playlist_id}/")
    Single<GetPlaylistResponse> getPlaylist(@Path("user_id") String userId,
            @Path("playlist_id") String playlistId);

    @GET(value = "/v1/users/{user_id}/playlists/{playlist_id}/tracks")
    Single<SpotifyPagingObject<SpotifyPlaylistTrack>> getPlaylistTracks(
            @Path("user_id") String userId, @Path("playlist_id") String playlistId,
            @Query("offset") Integer offset, @Query("limit") Integer limit);

    @POST(value = "/v1/users/{user_id}/playlists/{playlist_id}/tracks")
    Single<AddTrackToPlaylistResponse> addTrackToPlaylist(@Path("user_id") String userId,
            @Path("playlist_id") String playlistId, @Query("uris") List<String> uris);

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
            @Header("Authorization") String basic, @Field("grant_type") TokenGrantTypes grantTypes,
            @Field("code") String code, @Field("redirect_uri") String redirectUri);

    @POST
    @FormUrlEncoded
    Single<AuthorizationCodeResponse> refreshToken(@Url String url,
            @Header("Authorization") String basic, @Field("grant_type") TokenGrantTypes grantTypes,
            @Field("refresh_token") String refreshToken);
}
