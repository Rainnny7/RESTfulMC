/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny), and the RESTfulMC contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.mc.controller;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.exception.impl.BadRequestException;
import me.braydon.mc.exception.impl.MojangRateLimitException;
import me.braydon.mc.exception.impl.ResourceNotFoundException;
import me.braydon.mc.model.Player;
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
     * @throws BadRequestException       if the UUID or username is invalid
     * @throws ResourceNotFoundException if the player is not found
     * @throws MojangRateLimitException  if the Mojang API rate limit is reached
     */
    @GetMapping("/{query}")
    @ResponseBody
    public ResponseEntity<CachedPlayer> getPlayer(@PathVariable @NonNull String query)
            throws BadRequestException, ResourceNotFoundException, MojangRateLimitException
    {
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
        return ResponseEntity.ok()
                .contentType(extension.equalsIgnoreCase("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                .body(mojangService.getSkinPartTexture(partName, query, extension, size));
    }
}