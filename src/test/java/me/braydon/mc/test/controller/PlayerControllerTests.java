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
package me.braydon.mc.test.controller;

import lombok.NonNull;
import me.braydon.mc.controller.PlayerController;
import me.braydon.mc.test.config.TestRedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link PlayerController}.
 *
 * @author Braydon
 */
@SpringBootTest(classes = TestRedisConfig.class)
@AutoConfigureMockMvc
public final class PlayerControllerTests {
    /**
     * The {@link MockMvc} instance to use for testing.
     */
    @NonNull private final MockMvc mockMvc;

    @Autowired
    public PlayerControllerTests(@NonNull MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * Run a test to ensure retrieving
     * a player's data is successful.
     *
     * @throws Exception if the test fails
     */
    @Test
    void ensurePlayerLookupSuccess() throws Exception {
        mockMvc.perform(get("/player/g")
                        .accept(MediaType.APPLICATION_JSON) // Accept JSON
                        .contentType(MediaType.APPLICATION_JSON) // Content type is JSON
                ).andExpect(status().isOk()) // Expect 200 (OK)
                .andExpect(jsonPath("$.username").value("g")) // Expect the player's username
                .andReturn();
    }

    /**
     * Run a test to ensure retrieving
     * invalid player's results in a 404.
     *
     * @throws Exception if the test fails
     */
    @Test
    void ensurePlayerNotFound() throws Exception {
        mockMvc.perform(get("/player/SDFSDFSDFSDFDDDG")
                        .accept(MediaType.APPLICATION_JSON) // Accept JSON
                        .contentType(MediaType.APPLICATION_JSON) // Content type is JSON
                ).andExpect(status().isNotFound()) // Expect 404 (Not Found)
                .andReturn();
    }

    /**
     * Run a test to ensure retrieving
     * player's with invalid usernames
     * results in a 400.
     *
     * @throws Exception if the test fails
     */
    @Test
    void ensureUsernameIsInvalid() throws Exception {
        mockMvc.perform(get("/player/A")
                        .accept(MediaType.APPLICATION_JSON) // Accept JSON
                        .contentType(MediaType.APPLICATION_JSON) // Content type is JSON
                ).andExpect(status().isBadRequest()) // Expect 400 (Bad Request)
                .andReturn();
    }

    /**
     * Run a test to ensure retrieving
     * a player's head texture is successful.
     *
     * @throws Exception if the test fails
     */
    @Test
    void ensureSkinPartTextureSuccess() throws Exception {
        mockMvc.perform(get("/player/head/Rainnny.png")
                        .contentType(MediaType.IMAGE_PNG) // Content type is PNG
                ).andExpect(status().isOk()) // Expect 200 (OK)
                .andReturn();
    }
}