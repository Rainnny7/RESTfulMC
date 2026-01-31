package cc.restfulmc.api.controller;

import cc.restfulmc.api.exception.impl.BadRequestException;
import cc.restfulmc.api.exception.impl.ResourceNotFoundException;
import cc.restfulmc.api.model.server.MinecraftServer;
import cc.restfulmc.api.model.cache.CachedMinecraftServer;
import cc.restfulmc.api.service.MojangService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * The controller for handling
 * {@link MinecraftServer} related requests.
 *
 * @author Braydon
 */
@RestController
@RequestMapping(value = "/server", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2(topic = "Server Controller")
@Tag(name = "Server Controller", description = "The controller for handling server related requests.")
public final class ServerController {
    /**
     * The Mojang service to use.
     */
    @NonNull private final MojangService mojangService;

    @Autowired
    public ServerController(@NonNull MojangService mojangService) {
        this.mojangService = mojangService;
    }

    /**
     * Get a Minecraft server by its platform and hostname.
     *
     * @param platform the platform of the server
     * @param hostname the hostname of the server
     * @return the server
     * @throws BadRequestException       if the hostname, platform, or port is invalid
     * @throws ResourceNotFoundException if the server isn't found
     */
    @GetMapping("/{platform}/{hostname}")
    @ResponseBody
    public ResponseEntity<CachedMinecraftServer> getServer(
            @Parameter(description = "The platform of the server", example = "java") @PathVariable @NonNull String platform,
            @Parameter(description = "The server hostname to lookup (Append :<port> for port)", example = "hypixel.net") @PathVariable @NonNull String hostname
    ) throws BadRequestException, ResourceNotFoundException {
        return ResponseEntity.ofNullable(mojangService.getMinecraftServer(platform, hostname));
    }

    /**
     * Check if the server with the
     * given hostname is blocked by Mojang.
     *
     * @param hostname the server hostname to check
     * @return whether the hostname is blocked
     */
    @GetMapping("/blocked/{hostname}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> isServerBlocked(
            @Parameter(description = "The hostname of the server", example = "hypixel.net") @PathVariable @NonNull String hostname
    ) {
        return ResponseEntity.ok(Map.of(
                "blocked", mojangService.isServerBlocked(hostname)
        ));
    }

    /**
     * Get the server icon of a Minecraft
     * server by its platform and hostname.
     *
     * @param hostname the hostname of the server
     * @return the server icon
     */
    @GetMapping(value = "/icon/{hostname}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getServerFavicon(
            @Parameter(description = "The server hostname to lookup (Append :<port> for port)", example = "hypixel.net") @PathVariable @NonNull String hostname
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=%s.png".formatted(hostname))
                .body(mojangService.getServerFavicon(hostname));
    }
}