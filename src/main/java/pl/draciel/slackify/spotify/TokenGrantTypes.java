package pl.draciel.slackify.spotify;

import javax.annotation.Nonnull;

enum TokenGrantTypes {

    AUTHORIZATION_CODE("authorization_code"), REFRESH_TOKEN("refresh_token");

    @Nonnull
    private final String type;

    TokenGrantTypes(@Nonnull final String type) {
        this.type = type;
    }

    @Nonnull
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
