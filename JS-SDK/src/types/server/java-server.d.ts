import {MinecraftServer} from "@/types/server/server";

/**
 * A cacheable {@link JavaMinecraftServer}.
 */
export interface CachedJavaMinecraftServer extends JavaMinecraftServer {
	/**
	 * The unix timestamp of when this
	 * server was cached, -1 if not cached.
	 */
	cached: number;
}

/**
 * A Java edition {@link MinecraftServer}.
 */
export interface JavaMinecraftServer extends MinecraftServer {
	/**
	 * The version information of this server.
	 */
	version: JavaVersion;

	/**
	 * The favicon of this server, undefined if none.
	 */
	favicon?: Favicon | undefined;

	/**
	 * The software of this server, present if query is on.
	 */
	software?: string | undefined;

	/**
	 * The plugins on this server, present if
	 * query is on and plugins are present.
	 */
	plugins?: Plugin[] | undefined;

	/**
	 * The Forge mod information for this server, undefined if none.
	 * <p>
	 * This is for servers on 1.12 or below.
	 * </p>
	 */
	modInfo?: ModInfo | undefined;

	/**
	 * The Forge mod information for this server, undefined if none.
	 * <p>
	 * This is for servers on 1.13 and above.
	 * </p>
	 */
	forgeData?: ForgeData | undefined;

	/**
	 * The main world of this server, present if query is on.
	 */
	world?: string | undefined;

	/**
	 * Does this server support querying?
	 */
	queryEnabled: boolean;

	/**
	 * Does this server preview chat?
	 *
	 * @see <a href="https://www.minecraft.net/es-mx/article/minecraft-snapshot-22w19a">This for more</a>
	 */
	previewsChat: boolean;

	/**
	 * Does this server enforce secure chat?
	 */
	enforcesSecureChat: boolean;

	/**
	 * Is this server preventing chat reports?
	 */
	preventsChatReports: boolean;

	/**
	 * Is this server on the list
	 * of blocked servers by Mojang?
	 *
	 * @see <a href="https://wiki.vg/Mojang_API#Blocked_Servers">Mojang API</a>
	 */
	mojangBanned: boolean;
}

/**
 * Version information for a server.
 */
type JavaVersion = {
	/**
	 * The version name of the server.
	 */
	name: string;

	/**
	 * The identified platform of the server, undefined if unknown.
	 */
	platform?: string | undefined;

	/**
	 * The protocol version of the server.
	 */
	protocol: number;

	/**
	 * A list of versions supported by this server.
	 */
	supportedVersions: number[];

	/**
	 * The name of the version for the protocol, undefined if unknown.
	 */
	protocolName?: string | undefined;
};

/**
 * The favicon for a server.
 */
type Favicon = {
	/**
	 * The raw Base64 encoded favicon.
	 */
	base64: string;

	/**
	 * The URL to the favicon.
	 */
	url: string;
};

/**
 * A plugin for a server.
 */
type Plugin = {
	/**
	 * The name of this plugin.
	 */
	name: string;

	/**
	 * The version of this plugin.
	 */
	version: string;
};

/**
 * Forge mod information for a server.
 * <p>
 * This is for servers on 1.12 or below.
 * </p>
 */
type ModInfo = {
	/**
	 * The type of modded server this is.
	 */
	type: string;

	/**
	 * The list of mods on this server, empty if none.
	 */
	mods: LegacyForgeMod[];
};

/**
 * A legacy Forge mod for a server.
 */
type LegacyForgeMod = {
	/**
	 * The name of this mod.
	 */
	name: string;

	/**
	 * The version of this mod.
	 */
	version: string;
};

/**
 * Forge information for a server.
 * <p>
 * This is for servers on 1.13 and above.
 * </p>
 */
type ForgeData = {
	/**
	 * The list of channels on this server, empty if none.
	 */
	channels: ForgeChannel[];

	/**
	 * The list of mods on this server, empty if none.
	 */
	mods: ModernForgeMod[];

	/**
	 * The version of the FML network.
	 */
	fmlNetworkVersion: number;

	/**
	 * Are the channel and mod lists truncated?
	 * <p>
	 * Legacy versions see truncated lists, modern
	 * versions ignore this truncated flag.
	 * </p>
	 */
	truncated: boolean;
};

/**
 * A Forge channel for a server.
 */
type ForgeChannel = {
	/**
	 * The name of this channel.
	 */
	name: string;

	/**
	 * The version of this channel.
	 */
	version: string;

	/**
	 * Whether this channel is required.
	 */
	required: boolean;
};

/**
 * A modern Forge mod for a server.
 */
type ModernForgeMod = {
	/**
	 * The name of this mod.
	 */
	name: string;

	/**
	 * The marker for this mod.
	 */
	marker: string;
};
