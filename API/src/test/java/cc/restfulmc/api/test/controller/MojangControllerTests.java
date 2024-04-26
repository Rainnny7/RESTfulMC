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
package cc.restfulmc.api.test.controller;

import cc.restfulmc.api.controller.MojangController;
import cc.restfulmc.api.test.config.TestRedisConfig;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link MojangController}.
 *
 * @author Braydon
 */
@SpringBootTest(classes = TestRedisConfig.class)
@AutoConfigureMockMvc
public final class MojangControllerTests {
    /**
     * The {@link MockMvc} instance to use for testing.
     */
    @NonNull private final MockMvc mockMvc;

    @Autowired
    public MojangControllerTests(@NonNull MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * Run a test to ensure retrieving
     * the status of Mojang servers is
     * successful.
     *
     * @throws Exception if the test fails
     */
    @Test
    void ensureServerStatusCheckSuccess() throws Exception {
        mockMvc.perform(get("/mojang/status")
                        .accept(MediaType.APPLICATION_JSON) // Accept JSON
                        .contentType(MediaType.APPLICATION_JSON) // Content type is JSON
                ).andExpect(status().isOk()) // Expect 200 (OK)
                .andReturn();
    }
}