package cc.restfulmc.api.common;

import cc.restfulmc.api.common.renderer.model.PlayerModelCoordinates;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Braydon
 */
@UtilityClass
public final class SkinUtils {
    /**
     * Upgrades a legacy 64×32 Minecraft skin to the modern 64×64 format (1.8+).
     * Legacy skins have no overlays — only base layer. Places the 64×32 in the top
     * half, mirrors the leg/arm to create the missing left leg and left arm base,
     * then clears all overlay regions to transparent.
     *
     * @param image the skin image (legacy 64×32 or already 64×64)
     * @return the image in 64×64 format
     */
    public static BufferedImage upgradeLegacySkin(@NotNull BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width == height) {
            return image;
        }
        if (height * 2 != width) {
            return image;
        }
        double scale = width / 64.0;
        int newH = height + (int) (32 * scale);
        BufferedImage upgraded = new BufferedImage(width, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = upgraded.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        // Create missing left leg and left arm (mirror from legacy right leg/arm)
        for (int[] rect : PlayerModelCoordinates.LegacyUpgrade.LEFT_LEG_COPIES) {
            ImageUtils.copyRect(upgraded, rect[0], rect[1], rect[2], rect[3], rect[4], rect[5], rect[6], rect[7], scale);
        }
        for (int[] rect : PlayerModelCoordinates.LegacyUpgrade.LEFT_ARM_COPIES) {
            ImageUtils.copyRect(upgraded, rect[0], rect[1], rect[2], rect[3], rect[4], rect[5], rect[6], rect[7], scale);
        }

        // Clear overlay regions (HEADZ, BODYZ, RAZ, LAZ, LLZ) — legacy skins have no overlays
        for (int[] region : PlayerModelCoordinates.LegacyUpgrade.CLEAR_OVERLAYS) {
            ImageUtils.setAreaTransparentIfOpaque(upgraded, region[0], region[1], region[2], region[3], scale);
        }
        return upgraded;
    }

    /**
     * Get the ID of the skin from the given URL.
     *
     * @param url the url of the skin
     * @return the id of the skin
     */
    @NonNull
    public static String getId(@NonNull String url) {
        String[] urlSplit = url.split("/");
        return urlSplit[urlSplit.length - 1];
    }
}