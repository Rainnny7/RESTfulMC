package cc.restfulmc.api.model.token.server;

import cc.restfulmc.api.model.server.java.ForgeData;
import cc.restfulmc.api.model.server.java.JavaMinecraftServer;
import cc.restfulmc.api.model.server.java.ModInfo;
import cc.restfulmc.api.model.server.java.Version;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A token representing the response from
 * pinging a {@link JavaMinecraftServer}.
 *
 * @author Braydon
 */
@Getter @ToString(callSuper = true)
public final class JavaServerStatusToken extends GenericJavaServerStatusToken {
    /**
     * The base64 encoded favicon of this server, null if no favicon.
     */
    private final String favicon;

    /**
     * The Forge mod information for this server, null if none.
     * <p>
     * This is for servers on 1.12 or below.
     * </p>
     */
    @SerializedName("modinfo") private final ModInfo modInfo;

    /**
     * The Forge mod information for this server, null if none.
     * <p>
     * This is for servers on 1.13 and above.
     * </p>
     */
    private final ForgeData forgeData;

    /**
     * Does this server preview chat?
     *
     * @see <a href="https://www.minecraft.net/es-mx/article/minecraft-snapshot-22w19a">This for more</a>
     */
    private final boolean previewsChat;

    /**
     * Does this server enforce secure chat?
     */
    private final boolean enforcesSecureChat;

    /**
     * Is this server preventing chat reports?
     */
    private final boolean preventsChatReports;

    /**
     * Whether this is a modded server.
     */
    private final boolean isModded;

    public JavaServerStatusToken(@NonNull Object description, @NonNull Players players, @NonNull Version version, String favicon,
                                 ForgeData forgeData, boolean previewsChat, boolean enforcesSecureChat, boolean preventsChatReports,
                                 boolean isModded, ModInfo modInfo) {
        super(description, players, version);
        this.favicon = favicon;
        this.forgeData = forgeData;
        this.previewsChat = previewsChat;
        this.enforcesSecureChat = enforcesSecureChat;
        this.preventsChatReports = preventsChatReports;
        this.isModded = isModded;
        this.modInfo = modInfo;
    }
}