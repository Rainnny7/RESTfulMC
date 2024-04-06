package me.braydon.mc.controller;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.exception.impl.BadRequestException;
import me.braydon.mc.exception.impl.ResourceNotFoundException;
import me.braydon.mc.model.cache.CachedPlayer;
import me.braydon.mc.service.MojangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The controller for handling
 * player related requests.
 *
 * @author Braydon
 */
@RestController
@RequestMapping(value = "/player", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2(topic = "Player Controller")
public final class PlayerController {
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
}