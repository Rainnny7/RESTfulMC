package cc.restfulmc.api.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Braydon
 */
@UtilityClass
public final class MiscUtils {
    private static final Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");
    private static final List<String> WHITELISTED_USERNAMES = Arrays.asList("8", "g");

    /**
     * Check if the given username is a valid.
     *
     * @param username the username to check
     * @return whether the username is valid
     */
    public static boolean isUsernameValid(@NonNull String username) {
        return WHITELISTED_USERNAMES.contains(username.toLowerCase()) || USERNAME_REGEX.matcher(username).matches();
    }
}