package pl.draciel.slackify.spotify;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.draciel.slackify.Config;
import pl.draciel.slackify.security.*;
import pl.draciel.slackify.utility.StringUtil;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

@Configuration
class SpotifyConfig {

    @Bean
    @Nonnull
    SpotifyFacade provideSpotifyFacade(@Nonnull final Config config, @Nonnull final SpotifyService spotifyService,
                                       @Nonnull @Spotify final OAuth2Interceptor interceptor,
                                       @Nonnull final StateGenerator stateGenerator) {
        if (!StringUtil.isNullOrEmpty(config.getSpotifyDebugAccessToken())) {
            final OAuth2Token token = new OAuth2Token(config.getSpotifyDebugAccessToken(), null,
                    LocalDateTime.now().plusMinutes(30));
            interceptor.setOAuthToken(token);
        }
        return new SpotifyFacade(config, spotifyService, interceptor, stateGenerator);
    }

    @Bean
    @Spotify
    @Nonnull
    OAuth2Interceptor provideSpotifyInterceptor() {
        return new OAuth2Interceptor(SpotifyService.BASE_URL);
    }

    @Bean
    @Nonnull
    SpotifyService provideSpotifyService(@Spotify final OAuth2Interceptor interceptor) {
        final HttpLoggingInterceptor bodyInterceptor = new HttpLoggingInterceptor();
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(bodyInterceptor)
                .build();

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(SpotifyService.BASE_URL)
                .client(client)
                .build()
                .create(SpotifyService.class);
    }

    @Bean
    @Nonnull
    StateGenerator provideStateGenerator() {
        return StateGenerator.create();
    }

}
