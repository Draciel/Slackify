package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
class Offset {

    @Nullable
    @JsonProperty("position")
    private final Integer position;

    @Nullable
    @JsonProperty("uri")
    private final String uri;

}
