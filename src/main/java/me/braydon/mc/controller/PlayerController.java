package me.braydon.mc.controller;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
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
    private static final int DEFAULT_HEAD_SIZE = 128;
    private static final int MAX_HEAD_SIZE = 512;

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
     * @throws BadRequestException if the UUID is malformed
     * @throws ResourceNotFoundException if the player is not found
     */
    @GetMapping("/{query}")
    @ResponseBody
    public ResponseEntity<CachedPlayer> getPlayer(@PathVariable @NonNull String query) throws BadRequestException, ResourceNotFoundException {
        return ResponseEntity.ofNullable(mojangService.getPlayer(query, false));
    }

    /**
     * Get the head texture of a player
     * by their username or UUID.
     * <p>
     * If the player being searched is
     * invalid, the default Steve skin
     * will be used.
     * </p>
     *
     * @param query the query to search for the player by
     * @param extension the head image extension
     * @param size the size of the head image
     * @return the head texture
     * @throws BadRequestException if the extension is invalid
     */
    @GetMapping("/head/{query}.{extension}")
    @ResponseBody
    public ResponseEntity<byte[]> getHead(@PathVariable @NonNull String query, @PathVariable @NonNull String extension,
                                          @RequestParam(required = false) Integer size
    ) throws BadRequestException {
        if ((extension = extension.trim()).isBlank()) { // Invalid extension
            throw new BadRequestException("Invalid extension");
        }
        if (size == null || size <= 0) { // Invalid size
            size = DEFAULT_HEAD_SIZE;
        }
        size = Math.min(size, MAX_HEAD_SIZE); // Limit the size to 512

        Skin target = null; // The target skin to get the head of
        try {
            CachedPlayer player = mojangService.getPlayer(query, false); // Retrieve the player
            target = player.getSkin(); // Use the player's skin
        } catch (Exception ignored) {
            // Simply ignore, and fallback to the default skin
        }
        if (target == null) { // Fallback to the default skin
            target = Skin.DEFAULT_STEVE;
        }
        // Return the head texture
        return ResponseEntity.ok()
                .contentType(extension.equalsIgnoreCase("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                .body(PlayerUtils.getHeadTexture(target, size));
    }
}