package pl.draciel.slackify;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

@Configuration
class AppConfig {

    @Bean
    @Nonnull
    Config config() {
        final boolean usesRuntimeVars = Boolean.parseBoolean(System.getenv(AppKeys.USES_RUNTIME_VARS));

        if (usesRuntimeVars) {
            return Config.builder()
                    .spotifyClientId(System.getenv(AppKeys.SPOTIFY_CLIENT_ID))
                    .spotifyClientSecret(System.getenv(AppKeys.SPOTIFY_CLIENT_SECRET))
                    .redirectUri(System.getenv(AppKeys.REDIRECT_URI))
                    .userId(System.getenv(AppKeys.USER_ID))
                    .playlistUrl(System.getenv(AppKeys.PLAYLIST_URL))
                    .playlistId(System.getenv(AppKeys.PLAYLIST_ID))
                    .teamId(System.getenv(AppKeys.TEAM_ID))
                    .slackToken(System.getenv(AppKeys.SLACK_TOKEN))
                    .spotifyDebugAccessToken(System.getenv(AppKeys.SPOTIFY_DEBUG_ACCESS_TOKEN))
                    .build();
        }

        return Config.builder()
                .spotifyClientId(BuildConfig.SPOTIFY_CLIENT_ID)
                .spotifyClientSecret(BuildConfig.SPOTIFY_CLIENT_SECRET)
                .redirectUri(BuildConfig.REDIRECT_URI)
                .userId(BuildConfig.USER_ID)
                .playlistUrl(BuildConfig.PLAYLIST_URL)
                .playlistId(BuildConfig.PLAYLIST_ID)
                .teamId(BuildConfig.TEAM_ID)
                .slackToken(BuildConfig.SLACK_TOKEN)
                .spotifyDebugAccessToken(BuildConfig.SPOTIFY_DEBUG_ACCESS_TOKEN)
                .build();
    }
}
