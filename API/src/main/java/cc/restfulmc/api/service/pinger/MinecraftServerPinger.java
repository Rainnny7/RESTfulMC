package cc.restfulmc.api.service.pinger;

import cc.restfulmc.api.model.dns.DNSRecord;
import cc.restfulmc.api.model.server.MinecraftServer;
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