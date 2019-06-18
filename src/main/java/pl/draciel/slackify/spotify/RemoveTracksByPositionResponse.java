package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;

import javax.annotation.Nullable;
import java.util.List;

@Data
@Wither
@AllArgsConstructor
class RemoveTracksByPositionResponse {

    //fixme add nonnulls

    @JsonProperty("snapshot_id")
    private final String snapshotId;

    @Nullable
    private final List<Integer> positions;
}
