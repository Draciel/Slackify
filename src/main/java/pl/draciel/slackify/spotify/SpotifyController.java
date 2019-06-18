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

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/spotify")
class SpotifyController {

    @Nonnull
    private final SpotifyFacade spotifyFacade;

    @GetMapping(value = "/authorize")
    protected Single<RedirectView> authorize() {
        return spotifyFacade.authorize(Arrays.asList("playlist-modify-public", "playlist-modify-private"))
                .map(RedirectView::new);
    }

    @GetMapping(value = "/callback")
    protected Completable callback(@RequestParam String code, @RequestParam String state) {
        return spotifyFacade.grantTokens(code, state)
                .ignoreElement();
    }
}
