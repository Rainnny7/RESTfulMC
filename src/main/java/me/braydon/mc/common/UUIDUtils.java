package me.braydon.mc.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * @author Braydon
 */
@UtilityClass
public final class UUIDUtils {
    /**
     * Add dashes to an untrimmed uuid.
     *
     * @param trimmed the untrimmed uuid
     * @return the uuid with dashes
     */
    @NonNull
    public static UUID addDashes(@NonNull String trimmed) {
        StringBuilder builder = new StringBuilder(trimmed);
        for (int i = 0, pos = 20; i < 4; i++, pos -= 4) {
            builder.insert(pos, "-");
        }
        return UUID.fromString(builder.toString());
    }
}