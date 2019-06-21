package pl.draciel.slackify.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class SlashCommand {

    //fixme add nonnulls

    @JsonProperty("token")
    private final String slackToken;

    @JsonProperty("response_url")
    private final String responseUrl;

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
    @Nullable
    @JsonProperty("text")
    private final String parameters;

}
