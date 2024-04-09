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
package me.braydon.mc.common;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.xbill.DNS.Record;
import org.xbill.DNS.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author Braydon
 */
@UtilityClass
public final class DNSUtils {
    private static final String SRV_QUERY_PREFIX = "_minecraft._tcp.%s";

    /**
     * Resolve the hostname to an {@link InetSocketAddress}.
     *
     * @param hostname the hostname to resolve
     * @return the resolved {@link InetSocketAddress}
     */
    @SneakyThrows
    public static InetSocketAddress resolveSRV(@NonNull String hostname) {
        Record[] records = new Lookup(SRV_QUERY_PREFIX.formatted(hostname), Type.SRV).run(); // Resolve SRV records
        if (records == null) { // No records exist
            return null;
        }
        String host = null;
        int port = -1;
        for (Record record : records) {
            SRVRecord srv = (SRVRecord) record;
            host = srv.getTarget().toString().replaceFirst("\\.$", "");
            port = srv.getPort();
        }
        return host == null ? null :  new InetSocketAddress(host, port);
    }

    @SneakyThrows
    public static InetAddress resolveA(@NonNull String hostname) {
        Record[] records = new Lookup(hostname, Type.A).run(); // Resolve A records
        if (records == null) { // No records exist
            return null;
        }
        InetAddress address = null;
        for (Record record : records) {
            address = ((ARecord) record).getAddress();
        }
        return address;
    }
}