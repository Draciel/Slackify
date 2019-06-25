package pl.draciel.slackify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data
@Builder
@AllArgsConstructor
public class Config {

    @Nonnull
    private final String spotifyClientId;

    @Nonnull
    private final String spotifyClientSecret;

    @Nullable
    private final String slackClientId;

    @Nullable
    private final String slackClientSecret;

    @Nonnull
    private final String redirectUri;

    @Nonnull
    private final String userId;

    @Nonnull
    private final String playlistId;

    @Nonnull
    private final String teamId;

    @Nonnull
    private final String playlistUrl;

    @Nonnull
    private final String slackToken;

    @Nullable
    private final String spotifyDebugAccessToken;
}
