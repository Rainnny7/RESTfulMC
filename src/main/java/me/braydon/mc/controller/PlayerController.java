package me.braydon.mc.controller;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.common.EnumUtils;
import me.braydon.mc.common.PlayerUtils;
import me.braydon.mc.exception.impl.BadRequestException;
import me.braydon.mc.exception.impl.ResourceNotFoundException;
import me.braydon.mc.model.Player;
import me.braydon.mc.model.Skin;
import me.braydon.mc.model.cache.CachedPlayer;
import me.braydon.mc.service.MojangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The controller for handling
 * {@link Player} related requests.
 *
 * @author Braydon
 */
@RestController
@RequestMapping(value = "/player", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2(topic = "Player Controller")
public final class PlayerController {
    private static final int DEFAULT_PART_TEXTURE_SIZE = 128;
    private static final int MAX_PART_TEXTURE_SIZE = 512;

    /**
     * The Mojang service to use for player information.
     */
    @NonNull private final MojangService mojangService;

    @Autowired
    public PlayerController(@NonNull MojangService mojangService) {
        this.mojangService = mojangService;
    }

    /**
     * A GET route to get a player by their username or UUID.
     *
     * @param query the query to search for the player by
     * @return the player response
     * @throws BadRequestException       if the UUID is malformed
     * @throws ResourceNotFoundException if the player is not found
     */
    @GetMapping("/{query}")
    @ResponseBody
    public ResponseEntity<CachedPlayer> getPlayer(@PathVariable @NonNull String query) throws BadRequestException, ResourceNotFoundException {
        return ResponseEntity.ofNullable(mojangService.getPlayer(query, false));
    }

    /**
     * Get the part of a skin texture for
     * a player by their username or UUID.
     * <p>
     * If the player being searched is
     * invalid, the default Steve skin
     * will be used.
     * </p>
     *
     * @param partName  the part of the player's skin texture to get
     * @param query     the query to search for the player by
     * @param extension the skin part image extension
     * @param size      the size of the skin part image
     * @return the skin part texture
     * @throws BadRequestException if the extension is invalid
     */
    @GetMapping("/{partName}/{query}.{extension}")
    @ResponseBody
    public ResponseEntity<byte[]> getPartTexture(@PathVariable @NonNull String partName, @PathVariable @NonNull String query,
                                                 @PathVariable @NonNull String extension, @RequestParam(required = false) Integer size
    ) throws BadRequestException {
        Skin.Part part = EnumUtils.getEnumConstant(Skin.Part.class, partName.toUpperCase()); // The skin part to get
        if (part == null) { // Default to the head part
            part = Skin.Part.HEAD;
        }
        if ((extension = extension.trim()).isBlank()) { // Invalid extension
            throw new BadRequestException("Invalid extension");
        }
        if (size == null || size <= 0) { // Invalid size
            size = DEFAULT_PART_TEXTURE_SIZE;
        }
        size = Math.min(size, MAX_PART_TEXTURE_SIZE); // Limit the size to 512

        Skin target = null; // The target skin to get the skin part of
        try {
            CachedPlayer player = mojangService.getPlayer(query, false); // Retrieve the player
            target = player.getSkin(); // Use the player's skin
        } catch (Exception ignored) {
            // Simply ignore, and fallback to the default skin
        }
        if (target == null) { // Fallback to the default skin
            target = Skin.DEFAULT_STEVE;
        }
        // Return the skin part texture
        return ResponseEntity.ok()
                .contentType(extension.equalsIgnoreCase("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                .body(PlayerUtils.getSkinPart(target, part, size));
    }
}