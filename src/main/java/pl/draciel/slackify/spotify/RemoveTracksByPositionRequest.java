package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
class RemoveTracksByPositionRequest {

    //fixme add nonnulls

    @JsonProperty("positions")
    private final List<Integer> positions;

    @JsonProperty("snapshot_id")
    private final String snapshotId;
}
