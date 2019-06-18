package pl.draciel.slackify.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@Builder
class SlackRequestBody {

    //fixme add nonnulls

    @JsonProperty("token")
    private final String slackToken;

    @JsonProperty("team_id")
    private final String teamId;

    @JsonProperty("team_domain")
    private final String teamDomain;

    @JsonProperty("channel_id")
    private final String channelId;

    @JsonProperty("channel_name")
    private final String channelName;

    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("user_name")
    private final String userName;

    @JsonProperty("command")
    private final String command;

    //command parameters
    @JsonProperty("text")
    @Nullable
    private final String parameters;

    @JsonProperty("response_url")
    private final String responseUrl;
}
