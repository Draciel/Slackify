package pl.draciel.slackify.spotify.exceptions;

public class TrackNotFound extends RuntimeException {

    public TrackNotFound() {
    }

    public TrackNotFound(String s) {
        super(s);
    }

}
