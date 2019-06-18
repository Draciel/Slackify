package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;

@Data
@AllArgsConstructor
class SpotifyPagingObject<T> {

    //fixme add nonnulls

    @JsonProperty("href")
    private final String href;

    @JsonProperty("items")
    private final List<T> items;

    @JsonProperty("limit")
    private final int limit;

    @Nullable
    @JsonProperty("next")
    private final String next;

    @JsonProperty("offset")
    private final int offset;

    @Nullable
    @JsonProperty("previous")
    private final String previous;

    @JsonProperty("total")
    private final int total;
}
