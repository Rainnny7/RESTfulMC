package cc.restfulmc.api.model.server;

import cc.restfulmc.api.service.pinger.MinecraftServerPinger;
import cc.restfulmc.api.service.pinger.impl.BedrockMinecraftServerPinger;
import cc.restfulmc.api.service.pinger.impl.JavaMinecraftServerPinger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * A platform a Minecraft can operate on.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public enum ServerPlatform {
    /**
     * The Java edition of Minecraft.
     */
    JAVA(new JavaMinecraftServerPinger(), 25565),

    /**
     * The Bedrock edition of Minecraft.
     */
    BEDROCK(new BedrockMinecraftServerPinger(), 19132);

    /**
     * The server pinger for this platform.
     */
    @NonNull private final MinecraftServerPinger<?> pinger;

    /**
     * The default server port for this platform.
     */
    private final int defaultPort;
}