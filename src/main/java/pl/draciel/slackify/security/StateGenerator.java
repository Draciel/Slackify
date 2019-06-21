package pl.draciel.slackify.security;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Slf4j
public final class StateGenerator {

    private StateGenerator() {
        //no instance
    }

    public static StateGenerator create() {
        return new StateGenerator();
    }

    @Nonnull
    public String generateState() {
        final SecureRandom random = new SecureRandom();
        final byte[] seed = random.generateSeed(48);
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(seed);
            return EncryptionUtil.convertByteToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("No algorithm found!", e);
        }
        return EncryptionUtil.convertByteToHex(seed);
    }
}
