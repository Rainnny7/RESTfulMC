package cc.restfulmc.api.common.color;

import java.awt.*;

/**
 * Result of parsing a hex color: the color and the number of characters consumed.
 */
public record HexColorResult(Color color, int charsConsumed) {}
