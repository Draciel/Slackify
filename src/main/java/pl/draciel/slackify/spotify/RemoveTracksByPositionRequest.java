package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class RemoveTracksByPositionRequest {

    //fixme add nonnulls

    @JsonProperty("positions")
    private final List<Integer> positions;

    @JsonProperty("snapshot_id")
    private final String snapshotId;
}
