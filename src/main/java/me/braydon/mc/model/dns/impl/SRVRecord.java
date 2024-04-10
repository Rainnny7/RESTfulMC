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
package me.braydon.mc.model.dns.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import me.braydon.mc.model.dns.DNSRecord;

import java.net.InetSocketAddress;

/**
 * An SRV record implementation.
 *
 * @author Braydon
 */
@NoArgsConstructor @Setter @Getter @ToString(callSuper = true)
public final class SRVRecord extends DNSRecord {
    /**
     * The priority of this record.
     */
    private int priority;

    /**
     * The weight of this record.
     */
    private int weight;

    /**
     * The port of this record.
     */
    private int port;

    /**
     * The target of this record.
     */
    @NonNull private String target;

    public SRVRecord(@NonNull org.xbill.DNS.SRVRecord bootstrap) {
        super(Type.SRV, bootstrap.getTTL());
        priority = bootstrap.getPriority();
        weight = bootstrap.getWeight();
        port = bootstrap.getPort();
        target = bootstrap.getTarget().toString().replaceFirst("\\.$", "");
    }

    /**
     * Get a socket address from
     * the target and port.
     *
     * @return the socket address
     */
    @NonNull @JsonIgnore
    public InetSocketAddress getSocketAddress() {
        return new InetSocketAddress(target, port);
    }
}