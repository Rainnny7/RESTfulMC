package me.braydon.mc.controller;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.model.MinecraftServer;
import me.braydon.mc.service.MojangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The controller for handling
 * {@link MinecraftServer} related requests.
 *
 * @author Braydon
 */
@RestController
@RequestMapping(value = "/server", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2(topic = "Server Controller")
public final class ServerController {
    /**
     * The Mojang service to use for server information.
     */
    @NonNull private final MojangService mojangService;

    @Autowired
    public ServerController(@NonNull MojangService mojangService) {
        this.mojangService = mojangService;
    }

    @GetMapping("/{platform}/{hostname}")
    @ResponseBody
    public ResponseEntity<MinecraftServer> getServer(@PathVariable @NonNull String platform,
                                                     @PathVariable @NonNull String hostname
    ) {
        return ResponseEntity.ofNullable(mojangService.getMinecraftServer(platform, hostname));
    }
}