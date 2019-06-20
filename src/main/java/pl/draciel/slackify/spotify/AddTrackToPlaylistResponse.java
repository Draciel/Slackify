package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class AddTrackToPlaylistResponse {

    //fixme add nonnulls

    @JsonProperty("snapshot_id")
    private final String snapshotId;

    @Nullable
    private final String trackId;
}
