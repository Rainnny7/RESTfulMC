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