package pl.draciel.slackify.spotify;

import pl.draciel.slackify.spotify.model.Track;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Simple wrapper which prevents using {@link #playlist} directly.
 */
@ThreadSafe
class PlaylistCache {

    @Nonnull
    @GuardedBy("lock")
    private final List<Track> playlist = new LinkedList<>();

    @Nonnull
    private final Object lock = new Object();

    @Nullable
    public Track get(final int pos) {
        final Track t;
        synchronized (lock) {
            t = playlist.get(pos);
        }
        return t;
    }

    public void add(@Nonnull final Track track) {
        synchronized (lock) {
            playlist.add(track);
        }
    }

    @Nullable
    public Track remove(final int pos) {
        final Track t;
        synchronized (lock) {
            t = playlist.remove(pos);
        }
        return t;
    }

    public void cleanAndAddAll(@Nonnull final List<Track> tracks) {
        synchronized (lock) {
            playlist.clear();
            playlist.addAll(tracks);
        }
    }

    public boolean anyMatches(@Nonnull final Predicate<Track> predicate) {
        final boolean result;
        synchronized (lock) {
            result = playlist.stream().anyMatch(predicate);
        }
        return result;
    }

    public void addAll(@Nonnull final List<Track> tracks) {
        synchronized (lock) {
            playlist.addAll(tracks);
        }
    }

    public int size() {
        synchronized (lock) {
            return playlist.size();
        }
    }
}
