package pl.draciel.slackify.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public final class OAuth2Token {

    @Nonnull
    private final String accessToken;

    @Nullable
    private final String refreshToken;

    @Nonnull
    private final LocalDateTime expires;

}
