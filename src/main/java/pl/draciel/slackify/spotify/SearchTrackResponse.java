package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class SearchTrackResponse {

    //fixme add nonnulls

    @JsonProperty("tracks")
    private final SpotifyPagingObject<SpotifyTrack> tracks;
}
