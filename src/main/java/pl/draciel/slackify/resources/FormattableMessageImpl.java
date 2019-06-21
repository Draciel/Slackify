package pl.draciel.slackify.resources;

import lombok.AllArgsConstructor;

import javax.annotation.Nonnull;
import java.util.Locale;

@AllArgsConstructor
public final class FormattableMessageImpl implements FormattableMessage {

    @Nonnull
    private final String message;

    @Nonnull
    @Override
    public String formatMessage(@Nonnull Object... args) {
        return String.format(Locale.getDefault(), message, args);
    }
}
