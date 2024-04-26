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
package cc.restfulmc.api.service.pinger;

import cc.restfulmc.api.model.MinecraftServer;
import cc.restfulmc.api.model.dns.DNSRecord;
import lombok.NonNull;

/**
 * A {@link MinecraftServerPinger} is
 * used to ping a {@link MinecraftServer}.
 *
 * @param <T> the type of server to ping
 * @author Braydon
 */
public interface MinecraftServerPinger<T extends MinecraftServer> {
    /**
     * Ping the server with the given hostname and port.
     *
     * @param hostname the hostname of the server
     * @param ip       the ip of the server, null if unresolved
     * @param port     the port of the server
     * @param records the DNS records of the server
     * @return the server that was pinged
     */
    T ping(@NonNull String hostname, String ip, int port, @NonNull DNSRecord[] records);
}