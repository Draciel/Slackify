package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class SpotifyDevice {
    @Nullable

    @JsonProperty("id")
    private final String id;

    @JsonProperty("is_active")
    private final boolean isActive;

    @JsonProperty("is_private_session")
    private final boolean isPrivateSession;

    @JsonProperty("is_restricted")
    private final boolean isRestricted;

    @Nonnull
    @JsonProperty("name")
    private final String name;

    @Nonnull
    @JsonProperty("type")
    private final SpotifyDeviceType type;

    @Nullable
    @JsonProperty("volume_percent")
    private final Integer currentVolume;

}
