package cc.restfulmc.api.controller;

import cc.restfulmc.api.exception.impl.BadRequestException;
import cc.restfulmc.api.exception.impl.MojangRateLimitException;
import cc.restfulmc.api.exception.impl.ResourceNotFoundException;
import cc.restfulmc.api.model.player.Player;
import cc.restfulmc.api.model.cache.CachedPlayer;
import cc.restfulmc.api.model.player.skin.SkinRendererType;
import cc.restfulmc.api.service.PlayerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * The controller for handling
 * {@link Player} related requests.
 *
 * @author Braydon
 */
@RestController
@RequestMapping(value = "/player", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2(topic = "Player Controller")
@Tag(name = "Player Controller", description = "The controller for handling player related requests.")
public final class PlayerController {
    /**
     * The player service to use.
     */
    @NonNull private final PlayerService playerService;

    @Autowired
    public PlayerController(@NonNull PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * A GET route to get a player by their username or UUID.
     *
     * @param query the query to search for the player by
     * @param signed whether the profile is signed
     * @return the player response
     * @throws BadRequestException       if the UUID or username is invalid
     * @throws ResourceNotFoundException if the player is not found
     * @throws MojangRateLimitException  if the Mojang API rate limit is reached
     */
    @GetMapping("/{query}")
    @ResponseBody
    public ResponseEntity<CachedPlayer> getPlayer(
            @Parameter(description = "The player username or UUID to get", example = "Rainnny") @PathVariable @NonNull String query,
            @Parameter(description = "Whether the profile is signed by Mojang") @RequestParam(required = false) boolean signed
    ) throws BadRequestException, ResourceNotFoundException, MojangRateLimitException {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(playerService.getPlayer(query, signed));
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
     * @param query     the query to search for the player by
     * @param partName  the part of the player's skin texture to get
     * @param extension the skin part image extension
     * @param size      the size of the skin part image
     * @return the skin part texture
     * @throws BadRequestException if the extension is invalid
     */
    @GetMapping(value = "/{query}/{partName}.{extension}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    @ResponseBody
    public ResponseEntity<byte[]> getPartTexture(
            @Parameter(description = "The player username or UUID to get", example = "Rainnny") @PathVariable @NonNull String query,
            @Parameter(
                    description = "The skin part to get the texture of",
                    schema = @Schema(implementation = SkinRendererType.class),
                    example = "head"
            ) @PathVariable @NonNull String partName,
            @Parameter(description = "The image extension", example = "png") @PathVariable @NonNull String extension,
            @Parameter(description = "Whether to render skin overlays") @RequestParam(required = false, defaultValue = "true") boolean overlays,
            @Parameter(description = "The size to scale the skin part texture to", example = "512") @RequestParam(required = false, defaultValue = "512") String size
    ) throws BadRequestException {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .contentType(extension.equalsIgnoreCase("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                .body(playerService.getSkinPartTexture(query, partName, extension, overlays, size));
    }
}