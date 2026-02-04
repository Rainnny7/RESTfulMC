package cc.restfulmc.api.controller;

import cc.restfulmc.api.exception.impl.BadRequestException;
import cc.restfulmc.api.exception.impl.ResourceNotFoundException;
import cc.restfulmc.api.model.server.cache.CachedMinecraftServer;
import cc.restfulmc.api.model.server.MinecraftServer;
import cc.restfulmc.api.model.server.ServerPlatform;
import cc.restfulmc.api.service.MojangService;
import cc.restfulmc.api.service.ServerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
     * The Server service to use.
     */
    @NonNull private final ServerService serverService;

    /**
     * The Mojang service to use.
     */
    @NonNull private final MojangService mojangService;

    @Autowired
    public ServerController(@NonNull ServerService serverService, @NonNull MojangService mojangService) {
        this.serverService = serverService;
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
    @GetMapping("/{platform}/{hostname}") @ResponseBody
    public ResponseEntity<CachedMinecraftServer> getServer(
            @Parameter(
                    description = "The platform of the server",
                    schema = @Schema(implementation = ServerPlatform.class),
                    example = "java"
            ) @PathVariable @NonNull String platform,
            @Parameter(description = "The server hostname to lookup (Append :<port> for port)", example = "hypixel.net") @PathVariable @NonNull String hostname
    ) throws BadRequestException, ResourceNotFoundException {
        return ResponseEntity.ofNullable(serverService.getMinecraftServer(platform, hostname));
    }

    /**
     * Check if the server with the
     * given hostname is blocked by Mojang.
     *
     * @param hostname the server hostname to check
     * @return whether the hostname is blocked
     */
    @GetMapping("/blocked/{hostname}") @ResponseBody
    public ResponseEntity<Map<String, Object>> isServerBlocked(
            @Parameter(description = "The hostname of the server", example = "hypixel.net") @PathVariable @NonNull String hostname
    ) {
        return ResponseEntity.ok(Map.of(
                "blocked", mojangService.isServerBlocked(hostname)
        ));
    }

    /**
     * Get the server icon of a Java
     * Minecraft server by hostname.
     *
     * @param hostname the hostname of the server
     * @return the server icon
     */
    @GetMapping(value = "/{hostname}/icon.png", produces = MediaType.IMAGE_PNG_VALUE) @ResponseBody
    public ResponseEntity<byte[]> getServerFavicon(
            @Parameter(description = "The server hostname to lookup (Append :<port> for port)", example = "hypixel.net") @PathVariable @NonNull String hostname
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=%s-icon.png".formatted(hostname))
                .body(serverService.getServerFavicon(hostname));
    }

    /**
     * Get the server MOTD of Minecraft server by
     * the given platform and hostname in PNG format.
     *
     * @param hostname the hostname of the server
     * @return the server icon
     */
    @GetMapping(value = "/{platform}/{hostname}/motd.png", produces = MediaType.IMAGE_PNG_VALUE) @ResponseBody
    public ResponseEntity<byte[]> getServerMotd(
            @Parameter(
                    description = "The platform of the server",
                    schema = @Schema(implementation = ServerPlatform.class),
                    example = "java"
            ) @PathVariable @NonNull String platform,
            @Parameter(description = "The server hostname to lookup (Append :<port> for port)", example = "hypixel.net") @PathVariable @NonNull String hostname,
            @Parameter(description = "The size to scale the motd texture to", example = "512") @RequestParam(required = false, defaultValue = "512") int size
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=%s-motd.png".formatted(hostname))
                .body(serverService.getServerMotd(platform, hostname, size));
    }

    /**
     * Get the server MOTD of Minecraft server by
     * the given platform and hostname in HTML format.
     *
     * @param hostname the hostname of the server
     * @return the server icon
     */
    @GetMapping(value = "/{platform}/{hostname}/motd.html", produces = MediaType.TEXT_HTML_VALUE) @ResponseBody
    public ResponseEntity<String> getServerMotdHtml(
            @Parameter(
                    description = "The platform of the server",
                    schema = @Schema(implementation = ServerPlatform.class),
                    example = "java"
            ) @PathVariable @NonNull String platform,
            @Parameter(description = "The server hostname to lookup (Append :<port> for port)", example = "hypixel.net") @PathVariable @NonNull String hostname,
            @Parameter(description = "The size to scale the motd texture to", example = "512") @RequestParam(required = false, defaultValue = "512") int size
    ) {
        MinecraftServer server = serverService.getMinecraftServer(platform, hostname).getValue();
        return ResponseEntity.ok(server.getMotd().generateHtmlPreview(server));
    }
}