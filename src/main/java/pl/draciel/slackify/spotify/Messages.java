package pl.draciel.slackify.spotify;

import pl.draciel.slackify.resources.FormattableMessage;
import pl.draciel.slackify.resources.FormattableMessageImpl;
import pl.draciel.slackify.resources.Message;
import pl.draciel.slackify.resources.MessageImpl;

final class Messages {

    static final Message TRACK_NOT_FOUND = new MessageImpl("Track not found");
    static final Message AUTHORIZATION_REQUIRED = new MessageImpl("Authorization required");
    static final Message GRANTED_TOKENS = new MessageImpl("Granted tokens!");
    static final Message INVALID_POSITION = new MessageImpl("Invalid position!");
    static final Message INVALID_STATE = new MessageImpl("Invalid Request State");
    static final FormattableMessage ALREADY_ON_PLAYLIST =
            new FormattableMessageImpl("Track \"%1$s\" is already on playlist");

    private Messages() {
        //no instance
    }

}
