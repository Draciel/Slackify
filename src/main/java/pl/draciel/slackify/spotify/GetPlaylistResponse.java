package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class GetPlaylistResponse {

    //fixme add nonnulls

    @JsonProperty("collaborative")
    private final boolean collaborative;

    @JsonProperty("href")
    private final String href;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("snapshot_id")
    private final String snapshotId;

    @JsonProperty("tracks")
    private final SpotifyPagingObject<SpotifyPlaylistTrack> tracks;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("uri")
    private final String uri;

}
