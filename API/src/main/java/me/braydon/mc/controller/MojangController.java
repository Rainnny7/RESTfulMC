/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
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

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.common.MojangServer;
import me.braydon.mc.exception.impl.BadRequestException;
import me.braydon.mc.service.MojangService;
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
    @GetMapping("/status")
    @ResponseBody
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