package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;

import javax.annotation.Nullable;

@Data
@Wither
@AllArgsConstructor
class AddTrackToPlaylistResponse {

    //fixme add nonnulls

    @JsonProperty("snapshot_id")
    private final String snapshotId;

    @Nullable
    private final String trackId;
}
