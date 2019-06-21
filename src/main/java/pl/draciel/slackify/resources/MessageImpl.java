package pl.draciel.slackify.resources;

import lombok.AllArgsConstructor;

import javax.annotation.Nonnull;

@AllArgsConstructor
public final class MessageImpl implements Message {

    @Nonnull
    public final String message;

    @Nonnull
    @Override
    public String message() {
        return message;
    }
}
