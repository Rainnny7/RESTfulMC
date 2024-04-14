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
package me.braydon.mc.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents a service provided by Mojang.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public enum MojangServer {
    SESSION("https://sessionserver.mojang.com"),
    API("https://api.mojang.com"),
    TEXTURES("https://textures.minecraft.net"),
    ASSETS("https://assets.mojang.com"),
    LIBRARIES("https://libraries.minecraft.net");

    private static final int STATUS_TIMEOUT = 7000;

    /**
     * The endpoint of this service.
     */
    @NonNull private final String endpoint;

    /**
     * Ping this service and get the status of it.
     *
     * @return the service status
     */
    @NonNull
    public Status getStatus() {
        try {
            InetAddress address = InetAddress.getByName(endpoint.substring(8));
            long before = System.currentTimeMillis();
            if (address.isReachable(STATUS_TIMEOUT)) {
                // The time it took to reach the host is 75% of
                // the timeout, consider it to be degraded.
                if ((System.currentTimeMillis() - before) > STATUS_TIMEOUT * 0.75D) {
                    return Status.DEGRADED;
                }
                return Status.ONLINE;
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ignored) {
            // We can safely ignore any errors, we're simply checking
            // if the host is reachable, if it's not, it's offline.
        }
        return Status.OFFLINE;
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