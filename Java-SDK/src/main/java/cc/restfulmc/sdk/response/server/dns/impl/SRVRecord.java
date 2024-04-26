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
package cc.restfulmc.sdk.response.server.dns.impl;

import cc.restfulmc.sdk.response.server.dns.DNSRecord;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * An SRV record implementation.
 *
 * @author Braydon
 */
@Getter @ToString(callSuper = true)
public final class SRVRecord extends DNSRecord {
    /**
     * The priority of this record.
     */
    private final int priority;

    /**
     * The weight of this record.
     */
    private final int weight;

    /**
     * The port of this record.
     */
    private final int port;

    /**
     * The target of this record.
     */
    @NonNull private final String target;

    public SRVRecord(@NonNull Type type, long ttl, int priority, int weight, int port, @NonNull String target) {
        super(type, ttl);
        this.priority = priority;
        this.weight = weight;
        this.port = port;
        this.target = target;
    }
}