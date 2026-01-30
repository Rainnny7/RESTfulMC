package cc.restfulmc.api.common;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * @author Braydon
 */
@UtilityClass
public final class EnumUtils {
    /**
     * Get the enum constant of the specified enum type with the specified name.
     *
     * @param enumType the enum type
     * @param name     the name of the constant to return
     * @param <T>      the type of the enum
     * @return the enum constant of the specified enum type with the specified name
     */
    public <T extends Enum<T>> T getEnumConstant(@NonNull Class<T> enumType, @NonNull String name) {
        try {
            return Enum.valueOf(enumType, name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}