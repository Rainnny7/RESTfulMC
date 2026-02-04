package cc.restfulmc.api.common.renderer.impl.server;

import cc.restfulmc.api.RESTfulMC;
import cc.restfulmc.api.common.GraphicsUtils;
import cc.restfulmc.api.common.ImageUtils;
import cc.restfulmc.api.common.color.ColorUtils;
import cc.restfulmc.api.common.color.HexColorResult;
import cc.restfulmc.api.common.color.MinecraftColor;
import cc.restfulmc.api.common.font.Fonts;
import cc.restfulmc.api.common.renderer.Renderer;
import cc.restfulmc.api.model.server.MinecraftServer;
import cc.restfulmc.api.model.server.Players;
import cc.restfulmc.api.model.server.java.JavaMinecraftServer;
import cc.restfulmc.api.service.ServerService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * Renders a Minecraft server preview image (like the in-game server list).
 *
 * @author Braydon
 */
@Slf4j
public final class ServerPreviewRenderer extends Renderer<MinecraftServer> {
    /**
     * The singleton instance.
     */
    public static final ServerPreviewRenderer INSTANCE = new ServerPreviewRenderer();

    /**
     * The scale factor for rendering.
     */
    private static final int SCALE = 3;

    /**
     * The padding around the content.
     */
    private static final int PADDING = 5;

    /**
     * The row width.
     */
    private static final int ROW_WIDTH = (305 * SCALE) + (PADDING * 2);

    /**
     * The row height.
     */
    private static final int ROW_HEIGHT = (32 * SCALE) + (PADDING * 2);

    /**
     * The icon size.
     */
    private static final int ICON_SIZE = 32 * SCALE;

    /**
     * The gap between icon and text.
     */
    private static final int ICON_TEXT_GAP = 3 * SCALE;

    /**
     * The status icon width.
     */
    private static final int STATUS_ICON_WIDTH = 10 * SCALE;

    /**
     * The status icon height.
     */
    private static final int STATUS_ICON_HEIGHT = 8 * SCALE;

    /**
     * The right spacing.
     */
    private static final int RIGHT_SPACING = 5 * SCALE;

    /**
     * The server background image.
     */
    private static BufferedImage SERVER_BACKGROUND;

    /**
     * The ping icon image.
     */
    private static BufferedImage PING_ICON;

    static {
        try {
            SERVER_BACKGROUND = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(RESTfulMC.class.getResourceAsStream("/icons/server-background.png")).readAllBytes()));
            PING_ICON = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(RESTfulMC.class.getResourceAsStream("/icons/ping.png")).readAllBytes()));
        } catch (Exception ex) {
            log.error("Failed to load server preview assets", ex);
        }
    }

    @Override @NonNull
    public BufferedImage render(@NonNull MinecraftServer server, int size) {
        BufferedImage texture = new BufferedImage(ROW_WIDTH, ROW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        BufferedImage favicon = getServerFavicon(server);

        Graphics2D graphics = texture.createGraphics();

        // Draw the background
        for (int backgroundX = 0; backgroundX < ROW_WIDTH + SERVER_BACKGROUND.getWidth(); backgroundX += SERVER_BACKGROUND.getWidth()) {
            for (int backgroundY = 0; backgroundY < ROW_HEIGHT + SERVER_BACKGROUND.getHeight(); backgroundY += SERVER_BACKGROUND.getHeight()) {
                graphics.drawImage(SERVER_BACKGROUND, backgroundX, backgroundY, null);
            }
        }
        graphics.setColor(new Color(0, 0, 0, 80));
        graphics.fillRect(0, 0, ROW_WIDTH, ROW_HEIGHT);

        int textX = PADDING + ICON_SIZE + ICON_TEXT_GAP;
        int fontAscent = Fonts.MINECRAFT.ascent();

        // Favicon
        BufferedImage faviconScaled = ImageUtils.resize(favicon, (double) ICON_SIZE / favicon.getWidth());
        graphics.drawImage(faviconScaled, PADDING, PADDING, ICON_SIZE, ICON_SIZE, null);

        // Hostname
        graphics.setColor(MinecraftColor.WHITE.toAwtColor());
        GraphicsUtils.drawStringWithStyle(graphics, Fonts.MINECRAFT, server.getHostname(), textX, PADDING + SCALE + fontAscent * SCALE, true, false, false, SCALE);

        // MOTD
        int motdLine1Top = PADDING + (12 * SCALE);
        int motdLine2Top = PADDING + (21 * SCALE);
        String[] rawMotd = server.getMotd().getRaw();
        if (rawMotd != null && rawMotd.length > 0) {
            drawMotdLine(graphics, rawMotd[0], textX, motdLine1Top + fontAscent * SCALE);
            if (rawMotd.length > 1) {
                drawMotdLine(graphics, rawMotd[1], textX, motdLine2Top + fontAscent * SCALE);
            }
        }

        // Status area: ping icon at right, status text (player count) to its left
        int statusIconX = ROW_WIDTH - STATUS_ICON_WIDTH - RIGHT_SPACING;
        BufferedImage pingIcon = ImageUtils.resize(PING_ICON, SCALE);
        graphics.drawImage(pingIcon, statusIconX, PADDING, STATUS_ICON_WIDTH, STATUS_ICON_HEIGHT, null);

        // Player count
        Players players = server.getPlayers();
        String playersOnline = players.getOnline() + "";
        String playersMax = players.getMax() + "";
        int onlineWidth = GraphicsUtils.stringWidthAtScale(Fonts.MINECRAFT, playersOnline, SCALE)
                + GraphicsUtils.stringWidthAtScale(Fonts.MINECRAFT, "/", SCALE)
                + GraphicsUtils.stringWidthAtScale(Fonts.MINECRAFT, playersMax, SCALE);
        int statusTextX = statusIconX - onlineWidth - RIGHT_SPACING;
        int statusTextY = PADDING + SCALE + fontAscent * SCALE;

        graphics.setColor(MinecraftColor.GRAY.toAwtColor());
        statusTextX = GraphicsUtils.drawStringWithStyle(graphics, Fonts.MINECRAFT, playersOnline, statusTextX, statusTextY, true, false, false, SCALE);
        graphics.setColor(MinecraftColor.DARK_GRAY.toAwtColor());
        statusTextX = GraphicsUtils.drawStringWithStyle(graphics, Fonts.MINECRAFT, "/", statusTextX, statusTextY, true, false, false, SCALE);
        graphics.setColor(MinecraftColor.GRAY.toAwtColor());
        GraphicsUtils.drawStringWithStyle(graphics, Fonts.MINECRAFT, playersMax, statusTextX, statusTextY, true, false, false, SCALE);

        graphics.dispose();
        return ImageUtils.resize(texture, (double) size / ROW_WIDTH);
    }

    /**
     * Draws a MOTD line with color code parsing.
     *
     * @param graphics the graphics context
     * @param line the MOTD line
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void drawMotdLine(@NonNull Graphics2D graphics, @NonNull String line, int x, int y) {
        graphics.setColor(MinecraftColor.GRAY.toAwtColor()); // Minecraft MOTD default
        int index = 0;
        int drawX = x;
        boolean bold = false;
        boolean italic = false;

        while (index < line.length()) {
            int colorIndex = line.indexOf("§", index);
            if (colorIndex == -1) {
                String remaining = line.substring(index);
                GraphicsUtils.drawStringWithStyle(graphics, Fonts.MINECRAFT, remaining, drawX, y, true, bold, italic, SCALE);
                break;
            }

            String textBeforeColor = line.substring(index, colorIndex);
            drawX = GraphicsUtils.drawStringWithStyle(graphics, Fonts.MINECRAFT, textBeforeColor, drawX, y, true, bold, italic, SCALE);

            // §x§R§R§G§G§B§B or §#RRGGBB (gradient support)
            HexColorResult hexResult = ColorUtils.parseHexColor(line, colorIndex);
            if (hexResult != null) {
                graphics.setColor(hexResult.getColor());
                index = colorIndex + hexResult.getCharsConsumed();
            } else if (colorIndex + 1 < line.length()) {
                char colorCode = Character.toLowerCase(line.charAt(colorIndex + 1));
                switch (colorCode) {
                    case 'l' -> bold = true;
                    case 'o' -> italic = true;
                    case 'r' -> {
                        graphics.setColor(MinecraftColor.GRAY.toAwtColor());
                        bold = false;
                        italic = false;
                    }
                    default -> {
                        MinecraftColor mcColor = MinecraftColor.getByCode(colorCode);
                        if (mcColor != null) {
                            graphics.setColor(mcColor.toAwtColor());
                        }
                    }
                }
                index = colorIndex + 2;
            } else {
                index = colorIndex + 1;
            }
        }
    }

    /**
     * Gets the favicon of a server.
     *
     * @param server the server to get the favicon of
     * @return the server favicon
     */
    @NonNull
    public BufferedImage getServerFavicon(@NonNull MinecraftServer server) {
        String favicon = null;
        if (server instanceof JavaMinecraftServer javaServer && javaServer.getFavicon() != null) {
            favicon = javaServer.getFavicon().getBase64();
        }
        return ImageUtils.base64ToImage(favicon == null ? ServerService.DEFAULT_SERVER_ICON : favicon);
    }
}
