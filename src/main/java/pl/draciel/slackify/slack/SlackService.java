package pl.draciel.slackify.slack;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface SlackService {

    String BASE_URL = "https://slack.com/";

    @GET(value = "/oauth/authorize")
    Single<Void> authorize(@Query("client_id") String clientId, @Query("scope") String scope,
                           @Query("state") String state, @Query("team") String team);

}
