package pl.draciel.slackify.security;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@ThreadSafe
public final class OAuth2Interceptor implements Interceptor, OAuth2TokenStore {

    @Nonnull
    private final String registeredToHost;

    @Nonnull
    private final Object lock = new Object();

    @GuardedBy("lock")
    private volatile OAuth2Token oAuth2Token;

    public OAuth2Interceptor(@Nonnull final String registeredToHost) {
        this.registeredToHost = registeredToHost;
    }

    @Nonnull
    @Override
    public Response intercept(@Nonnull final Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final OAuth2Token token;

        synchronized (lock) {
            token = oAuth2Token;
        }

        if (token == null || !originalRequest.url().toString().contains(registeredToHost) ||
                LocalDateTime.now().isAfter(oAuth2Token.getExpires())) {
            return chain.proceed(originalRequest);
        }

        return chain.proceed(originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + oAuth2Token.getAccessToken())
                .method(originalRequest.method(), originalRequest.body())
                .build());
    }

    @Override
    @Nullable
    public OAuth2Token getOAuthToken() {
        final OAuth2Token token;
        synchronized (lock) {
            token = oAuth2Token;
        }
        return token;
    }

    @Override
    public void setOAuthToken(@Nonnull final OAuth2Token oAuth2Token) {
        log.debug(oAuth2Token.toString());
        synchronized (lock) {
            this.oAuth2Token = oAuth2Token;
        }
    }
}
