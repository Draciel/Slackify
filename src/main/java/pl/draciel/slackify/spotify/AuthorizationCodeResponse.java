package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class AuthorizationCodeResponse {

    //fixme add nonnulls

    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("token_type")
    private final String tokenType;

    @JsonProperty("scope")
    private final String scope;

    @JsonProperty("expires_in")
    private final int expiresIn;

    @JsonProperty("refresh_token")
    private final String refreshToken;

}
