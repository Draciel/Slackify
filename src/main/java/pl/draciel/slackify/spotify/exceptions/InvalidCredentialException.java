package pl.draciel.slackify.spotify.exceptions;

public class InvalidCredentialException extends RuntimeException {

    public InvalidCredentialException() {
        super();
    }

    public InvalidCredentialException(String message) {
        super(message);
    }
}
