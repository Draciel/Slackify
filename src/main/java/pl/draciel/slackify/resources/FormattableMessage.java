package pl.draciel.slackify.resources;

import javax.annotation.Nonnull;

public interface FormattableMessage {
    @Nonnull
    String formatMessage(@Nonnull final Object... args);
}
