package pl.draciel.slackify.slack.exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
    }

    public UserNotFound(String s) {
        super(s);
    }
}
