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
package cc.restfulmc.api.common;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import cc.restfulmc.api.model.dns.impl.ARecord;
import cc.restfulmc.api.model.dns.impl.SRVRecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

/**
 * @author Braydon
 */
@UtilityClass
public final class DNSUtils {
    private static final String SRV_QUERY_PREFIX = "_minecraft._tcp.%s";

    /**
     * Get the resolved address and port of the
     * given hostname by resolving the SRV records.
     *
     * @param hostname the hostname to resolve
     * @return the resolved address and port, null if none
     */
    @SneakyThrows
    public static SRVRecord resolveSRV(@NonNull String hostname) {
        Record[] records = new Lookup(SRV_QUERY_PREFIX.formatted(hostname), Type.SRV).run(); // Resolve SRV records
        if (records == null) { // No records exist
            return null;
        }
        SRVRecord result = null;
        for (Record record : records) {
            result = new SRVRecord((org.xbill.DNS.SRVRecord) record);
        }
        return result;
    }

    /**
     * Get the resolved address of the given
     * hostname by resolving the A records.
     *
     * @param hostname the hostname to resolve
     * @return the resolved address, null if none
     */
    @SneakyThrows
    public static ARecord resolveA(@NonNull String hostname) {
        Record[] records = new Lookup(hostname, Type.A).run(); // Resolve A records
        if (records == null) { // No records exist
            return null;
        }
        ARecord result = null;
        for (Record record : records) {
            result = new ARecord((org.xbill.DNS.ARecord) record);
        }
        return result;
    }
}