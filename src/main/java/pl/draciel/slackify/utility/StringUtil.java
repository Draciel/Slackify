package pl.draciel.slackify.utility;

import javax.annotation.Nullable;

public final class StringUtil {

    private StringUtil() {
        //no instance
    }

    public static boolean isNullOrEmpty(@Nullable final String text) {
        return text == null || text.isEmpty();
    }
}
