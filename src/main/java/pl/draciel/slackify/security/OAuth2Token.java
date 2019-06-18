package pl.draciel.slackify.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public final class OAuth2Token {

    @Nonnull
    private final String accessToken;

    @Nonnull
    private final String refreshToken;

    @Nonnull
    private final LocalDateTime expires;

}
