package pl.draciel.slackify.spotify;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.annotations.SchedulerSupport;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.draciel.slackify.Config;
import pl.draciel.slackify.spotify.model.Device;
import pl.draciel.slackify.utility.StringUtil;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static pl.draciel.slackify.spotify.Messages.AUTHORIZATION_REQUIRED;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/spotify")
class SpotifyController {

    @Nonnull
    private static final List<String> AUTH_SCOPES =
            Collections.unmodifiableList(Arrays.asList("playlist-modify-public", "playlist-modify-private",
                    "user-modify-playback-state"));

    @Nonnull
    private final SpotifyFacade spotifyFacade;

    @Nonnull
    private final Config config;

    @GetMapping("/authorize")
    Single<RedirectView> authorize() {
        return spotifyFacade.authorize(AUTH_SCOPES)
                .map(RedirectView::new);
    }

    @GetMapping("/callback")
    Completable callback(@RequestParam("code") String code, @RequestParam("state") String state) {
        return spotifyFacade.grantTokens(code, state)
                .ignoreElement();
    }

    @PostMapping("/play")
    Completable playPlayer(@RequestParam("token") @Nullable final String token,
                           @RequestParam("device_id") @Nullable final String deviceId) {
        return validateToken(token)
                .andThen(spotifyFacade.playPlayer(deviceId));
    }

    @PostMapping("/pause")
    Completable pausePlayer(@RequestParam("token") @Nullable final String token,
                            @RequestParam("device_id") @Nullable final String deviceId) {
        return validateToken(token)
                .andThen(spotifyFacade.pausePlayer(deviceId));
    }

    @GetMapping("/devices")
    Single<List<Device>> getUserDevices(@RequestParam("token") @Nullable final String token) {
        return validateToken(token)
                .andThen(spotifyFacade.getUserDevices());
    }

    @PostMapping("/transfer")
    Completable transferPlayback(@RequestParam("token") @Nullable final String token,
                                 @RequestParam("device_id") @Nonnull final String deviceId) {
        return validateToken(token)
                .andThen(spotifyFacade.transferPlayback(deviceId));
    }

    @Nonnull
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    private Completable validateToken(@Nullable final String token) {
        if (StringUtil.isNullOrEmpty(config.getToken()) || Objects.equals(token, config.getToken())) {
            return Completable.complete();
        }
        return Completable.error(new IllegalStateException(AUTHORIZATION_REQUIRED.message()));
    }


}
