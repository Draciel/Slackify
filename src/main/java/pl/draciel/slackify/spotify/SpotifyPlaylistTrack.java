package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class SpotifyPlaylistTrack {

    //fixme add nonnulls

    @JsonProperty("added_at")
    private final String addedAt;

    @JsonProperty("is_local")
    private final boolean isLocal;

    @JsonProperty("track")
    private final SpotifyTrack track;

}
