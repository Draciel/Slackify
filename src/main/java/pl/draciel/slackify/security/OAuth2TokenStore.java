package pl.draciel.slackify.security;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface OAuth2TokenStore {

    @Nullable
    OAuth2Token getOAuthToken();

    void setOAuthToken(@Nonnull OAuth2Token oAuth2Token);
}
