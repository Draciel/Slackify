package pl.draciel.slackify.spotify;

import javax.annotation.Nonnull;

enum SpotifySearchQueryTypes {

    Album("album"), ARTIST("artist"), PLAYLIST("playlist"), TRACK("track");

    @Nonnull
    private final String type;

    SpotifySearchQueryTypes(@Nonnull final String type) {
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
