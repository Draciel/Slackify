package pl.draciel.slackify.slack;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.draciel.slackify.Config;
import pl.draciel.slackify.security.OAuth2Interceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

@Configuration
class SlackConfig {

    @Bean
    @Nonnull
    SlackFacade provideSlackFacade(@Nonnull final Config config,
                                   @Nonnull final AddTrackLogRepository addTrackLogRepository,
                                   @Nonnull final RemoveTrackLogRepository removeTrackLogRepository,
                                   @Nonnull final UserRepository userRepository) {
        return new SlackFacade(config, addTrackLogRepository, removeTrackLogRepository, userRepository);
    }

    @Bean
    @Slack
    @Nonnull
    OAuth2Interceptor provideSlackInterceptor() {
        return new OAuth2Interceptor(SlackService.BASE_URL);
    }

    @Bean
    @Nonnull
    SlackService provideSlackService(@Slack final OAuth2Interceptor interceptor) {
        final HttpLoggingInterceptor bodyInterceptor = new HttpLoggingInterceptor();
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(bodyInterceptor)
                .build();

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl(SlackService.BASE_URL)
                .client(client)
                .build()
                .create(SlackService.class);
    }

    @Configuration
    static class ConverterConfiguration implements WebMvcConfigurer {
        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            final SlashCommandConverter converter = new SlashCommandConverter();
            final MediaType mediaType = MediaType.APPLICATION_FORM_URLENCODED;
            converter.setSupportedMediaTypes(Collections.singletonList(mediaType));
            converters.add(converter);
        }
    }
}
