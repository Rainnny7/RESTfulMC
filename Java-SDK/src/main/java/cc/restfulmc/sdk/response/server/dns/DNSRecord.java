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
package cc.restfulmc.sdk.response.server.dns;

import cc.restfulmc.sdk.response.server.dns.impl.ARecord;
import cc.restfulmc.sdk.response.server.dns.impl.SRVRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A representation of a DNS record.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public abstract class DNSRecord {
    /**
     * The type of this record.
     */
    @NonNull private final Type type;

    /**
     * The TTL (Time To Live) of this record.
     */
    private final long ttl;

    /**
     * Types of a record.
     */
    @AllArgsConstructor @Getter
    public enum Type {
        A(ARecord.class),
        SRV(SRVRecord.class);

        /**
         * The record class for this type.
         */
        @NonNull private final Class<? extends DNSRecord> recordClass;
    }
}