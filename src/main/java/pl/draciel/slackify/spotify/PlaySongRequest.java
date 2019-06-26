package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class PlaySongRequest {

    /**
     * Accepts only albums/artists/playlist
     */
    @Nullable
    @JsonProperty("context_uri")
    private final String contextUri;

    /**
     * Tracks
     */
    @Nullable
    @JsonProperty("uris")
    private final List<String> uris;

    /**
     * Starting offset (position in playlist/album or just starting track)
     */
    @Nullable
    @JsonProperty("offset")
    private final Offset offset;

    /**
     * Position for playback in track in milliseconds
     */
    @Nullable
    @JsonProperty("position_ms")
    private final Integer position;
}
