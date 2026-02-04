package cc.restfulmc.api.common.color;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;

/**
 * Result of parsing a hex color: the color and the number of characters consumed.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public final class HexColorResult {
    /**
     * The parsed color.
     */
    @NonNull private final Color color;

    /**
     * The number of characters consumed from the input.
     */
    private final int charsConsumed;
}
