package pl.draciel.slackify.spotify.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {

    @Nullable
    private final String id;

    private final boolean isActive;

    @Nonnull
    private final String name;

    @Nonnull
    private final String deviceType;

    @Nullable
    private final Integer currentVolume;

}
