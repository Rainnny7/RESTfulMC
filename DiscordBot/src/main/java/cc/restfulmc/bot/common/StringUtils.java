package cc.restfulmc.bot.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * @author Braydon
 */
@UtilityClass
public final class StringUtils {
    /**
     * Capitalize the first character in the given string.
     *
     * @param input the input to capitalize
     * @return the capitalized string
     */
    @NonNull
    public static String capitalize(@NonNull String input) {
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }
}