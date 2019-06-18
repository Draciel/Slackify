package pl.draciel.slackify;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

@Configuration
class AppConfig {

    @Bean
    @Nonnull
    Config config() {
        return Config.builder()
                .spotifyClientId(BuildConfig.SPOTIFY_CLIENT_ID)
                .spotifyClientSecret(BuildConfig.SPOTIFY_CLIENT_SECRET)
                .redirectUri(BuildConfig.REDIRECT_URI)
                .userId(BuildConfig.USER_ID)
                .playlistUrl(BuildConfig.PLAYLIST_URL)
                .playlistId(BuildConfig.PLAYLIST_ID)
                .teamId(BuildConfig.TEAM_ID)
                .slackToken(BuildConfig.SLACK_TOKEN)
                .build();
    }

}
