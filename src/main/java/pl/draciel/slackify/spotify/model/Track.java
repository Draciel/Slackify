package pl.draciel.slackify.spotify.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data
@AllArgsConstructor
public class Track {

    @Nonnull
    private final String artist;

    @Nonnull
    private final String name;

    @Nullable
    private final String spotifyId;
}
