package pl.draciel.slackify.spotify;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;

enum SpotifyDeviceType {

    COMPUTER("Computer"),
    TABLET("Tablet"),
    SMARTPHONE("Smartphone"),
    SPEAKER("Speaker"),
    TV("TV"),
    AVR("AVR"),
    STB("STB"),
    AUDIO_DONGLE("AudioDongle"),
    GAME_CONSOLE("GameConsole"),
    CASTV_IDEO("CastVideo"),
    CAST_AUDIO("CastAudio"),
    AUTOMOBILE("Automobile"),
    UNKNOWN("Unknown");

    @Nonnull
    @JsonValue
    private final String type;

    SpotifyDeviceType(@Nonnull final String type) {
        this.type = type;
    }

    @Nonnull
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }

}
