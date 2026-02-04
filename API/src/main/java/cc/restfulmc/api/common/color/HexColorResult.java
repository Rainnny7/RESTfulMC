package cc.restfulmc.api.common.color;

import lombok.NonNull;

import java.awt.*;

/**
 * Result of parsing a hex color: the color and the number of characters consumed.
 *
 * @author Braydon
 */
public record HexColorResult(@NonNull Color color, int charsConsumed) {}
