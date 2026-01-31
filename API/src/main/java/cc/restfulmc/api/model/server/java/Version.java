package cc.restfulmc.api.model.server.java;

import cc.restfulmc.api.common.JavaMinecraftVersion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Version information for a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class Version {
    /**
     * The version name of the server.
     */
    @NonNull private final String name;

    /**
     * The identified platform of the server, null if unknown.
     */
    private String platform;

    /**
     * The protocol version of the server.
     */
    private final int protocol;

    /**
     * A list of versions supported by this server.
     */
    private final int[] supportedVersions;

    /**
     * The name of the version for the protocol, null if unknown.
     */
    private final String protocolName;

    /**
     * Create a more detailed
     * copy of this object.
     *
     * @return the detailed copy
     */
    @NonNull
    public Version detailedCopy() {
        String platform = null;
        if (name.contains(" ")) { // Parse the server platform
            String[] split = name.split(" ");
            if (split.length == 2) {
                platform = split[0];
            }
        }
        JavaMinecraftVersion minecraftVersion = JavaMinecraftVersion.byProtocol(protocol);
        if (minecraftVersion == JavaMinecraftVersion.UNKNOWN) {
            minecraftVersion = null;
        }
        return new Version(name, platform, protocol, supportedVersions, minecraftVersion == null ? null : minecraftVersion.getName());
    }
}