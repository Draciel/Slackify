package pl.draciel.slackify.slack;

import pl.draciel.slackify.resources.FormattableMessage;
import pl.draciel.slackify.resources.FormattableMessageImpl;
import pl.draciel.slackify.resources.Message;
import pl.draciel.slackify.resources.MessageImpl;

final class Messages {

    static final FormattableMessage TRACK_NOT_FOUND = new FormattableMessageImpl("\"%1$s\" not found");
    static final FormattableMessage ADDED_TO_PLAYLIST =
            new FormattableMessageImpl("%1$s by %2$s has been added to playlist!");
    static final Message REMOVED_ILLEGAL_TRACK =
            new MessageImpl("Someone is cheating right here, this track was added from SpotifyApp, you " +
                    "can't remove it, contact admin to force remove it ¯\\_(ツ)_/¯");
    static final FormattableMessage TRACK_WAS_ADDED_BY =
            new FormattableMessageImpl("Track was added by %1$s you can ask %1$s for remove it.");
    static final FormattableMessage SUCCESFULLY_REMOVED_TRACK =
            new FormattableMessageImpl("Track on position %1$s has been removed.");
    static final Message UNKNOWN_MESSAGE = new MessageImpl("Something went wrong, please try again later");
    static final Message USE_ARTIST_TRACK_FORMAT = new MessageImpl("Invalid request format. Please use Artist - Track");
    static final Message ARTIST_CANT_BE_EMPTY = new MessageImpl("Artist can't be empty");
    static final Message TRACK_CANT_BE_EMPTY = new MessageImpl("Track can't be empty");
    static final Message USE_NUMBER_FORMAT = new MessageImpl("Invalid request format. Please use number");
    static final Message INVALID_TRACK_POSITION = new MessageImpl("Invalid track position");
    static final Message USER_NOT_FOUND = new MessageImpl("User not found!");
    static final Message INVALID_SLACK_TOKEN = new MessageImpl("Invalid slack token");
    static final Message INVALID_TEAM_ID = new MessageImpl("Invalid team id");

    private Messages() {
        //no instance
    }

}
