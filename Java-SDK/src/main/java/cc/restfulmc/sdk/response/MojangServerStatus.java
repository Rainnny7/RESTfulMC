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
package cc.restfulmc.sdk.response;

import lombok.*;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public final class MojangServerStatus {
    /**
     * The servers to show the status of.
     */
    @NonNull private final MojangServer[] servers;

    @AllArgsConstructor @Getter @ToString
    public static class MojangServer {
        /**
         * The name of this server.
         */
        @NonNull private final String name;

        /**
         * The name of this server.
         */
        @NonNull private final String endpoint;

        /**
         * The status of this server.
         */
        @NonNull private final Status status;
    }

    /**
     * The status of a service.
     */
    public enum Status {
        /**
         * The service is online and accessible.
         */
        ONLINE,

        /**
         * The service is online, but is experiencing degraded performance.
         */
        DEGRADED,

        /**
         * The service is offline and inaccessible.
         */
        OFFLINE
    }
}