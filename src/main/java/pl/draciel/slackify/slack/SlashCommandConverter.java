package pl.draciel.slackify.slack;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;

class SlashCommandConverter extends AbstractHttpMessageConverter<SlashCommand> {

    @Nonnull
    private final FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

    @Nonnull
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean supports(@Nonnull final Class<?> clazz) {
        return clazz == SlashCommand.class;
    }

    @Nonnull
    @Override
    protected SlashCommand readInternal(@Nonnull final Class<? extends SlashCommand> clazz,
                                        @Nonnull final HttpInputMessage inputMessage)
            throws HttpMessageNotReadableException, IOException {
        final Map<String, String> requestParameters =
                formHttpMessageConverter.read(null, inputMessage).toSingleValueMap();
        return objectMapper.convertValue(requestParameters, SlashCommand.class);
    }

    @Override
    protected void writeInternal(@Nonnull final SlashCommand slashCommand,
                                 @Nonnull final HttpOutputMessage outputMessage)
            throws HttpMessageNotWritableException {
    }
}
