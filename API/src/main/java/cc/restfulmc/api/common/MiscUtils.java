package cc.restfulmc.api.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * @author Braydon
 */
@UtilityClass
public final class MiscUtils {
    private static final Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9_]{1,16}$");

    /**
     * Check if the given username is valid.
     *
     * @param username the username to check
     * @return whether the username is valid
     */
    public static boolean isUsernameValid(@NonNull String username) {
        return USERNAME_REGEX.matcher(username).matches();
    }
}