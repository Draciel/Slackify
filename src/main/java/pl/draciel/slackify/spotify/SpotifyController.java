package pl.draciel.slackify.spotify;

import io.reactivex.Completable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/spotify")
class SpotifyController {

    @Nonnull
    private static final List<String> AUTH_SCOPES =
            Collections.unmodifiableList(Arrays.asList("playlist-modify-public", "playlist-modify-private"));

    @Nonnull
    private final SpotifyFacade spotifyFacade;

    @GetMapping("/authorize")
    protected Single<RedirectView> authorize() {
        return spotifyFacade.authorize(AUTH_SCOPES)
                .map(RedirectView::new);
    }

    @GetMapping("/callback")
    protected Completable callback(@RequestParam("code") String code, @RequestParam("state") String state) {
        return spotifyFacade.grantTokens(code, state)
                .ignoreElement();
    }
}
