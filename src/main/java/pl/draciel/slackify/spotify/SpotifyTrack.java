package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class SpotifyTrack {

    //fixme add nonnulls

    @JsonProperty("artists")
    private final List<SpotifyArtist> artists;

    @JsonProperty("duration_ms")
    private final int duration;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;
}
