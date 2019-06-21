package pl.draciel.slackify.security;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class EncryptionUtil {

    private EncryptionUtil() {
        //no instance
    }

    static String convertByteToHex(@Nonnull final byte[] input) {
        return IntStream.range(0, input.length)
                .mapToObj(i -> Integer.toString((input[i] & 0xFF) + 0x100, 16).substring(1))
                .collect(Collectors.joining());
    }
}
