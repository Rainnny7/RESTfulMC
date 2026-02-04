package cc.restfulmc.api.controller;

import cc.restfulmc.api.common.MojangServer;
import cc.restfulmc.api.exception.impl.BadRequestException;
import cc.restfulmc.api.service.MojangService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller for handling
 * Mojang related requests.
 *
 * @author Braydon
 */
@RestController
@RequestMapping(value = "/mojang", produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2(topic = "Mojang Controller")
@Tag(name = "Mojang Controller", description = "The controller for handling Mojang related requests.")
public final class MojangController {
    /**
     * The Mojang service to use.
     */
    @NonNull private final MojangService mojangService;

    @Autowired
    public MojangController(@NonNull MojangService mojangService) {
        this.mojangService = mojangService;
    }

    /**
     * A GET route to get the status of Mojang servers.
     *
     * @return the status response
     */
    @GetMapping("/status") @ResponseBody
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getStatus() throws BadRequestException {
        List<Map<String, Object>> servers = new ArrayList<>();
        for (Map.Entry<MojangServer, MojangServer.Status> entry : mojangService.getMojangServerStatuses().entrySet()) {
            MojangServer server = entry.getKey();

            Map<String, Object> serverStatus = new HashMap<>();
            serverStatus.put("name", server.getName());
            serverStatus.put("endpoint", server.getEndpoint());
            serverStatus.put("status", entry.getValue().name());
            servers.add(serverStatus);
        }
        return ResponseEntity.ok(Map.of("servers", servers));
    }
}