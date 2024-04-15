import { MinecraftServer } from "@/types/server";

/**
 * A cacheable {@link BedrockMinecraftServer}.
 */
export interface CachedBedrockMinecraftServer extends BedrockMinecraftServer {
	/**
	 * The unix timestamp of when this
	 * server was cached, -1 if not cached.
	 */
	cached: number;
}

/**
 * A Bedrock edition {@link MinecraftServer}.
 */
export interface BedrockMinecraftServer extends MinecraftServer {
	/**
	 * The ID of this server.
	 */
	id: string;

	/**
	 * The edition of this server.
	 */
	edition: Edition;

	/**
	 * The version information of this server.
	 */
	version: BedrockVersion;

	/**
	 * The gamemode of this server.
	 */
	gamemode: GameMode;
}

/**
 * The edition of a Bedrock server.
 */
export type Edition = "MCPE" | "MCEE";

/**
 * Version information for a server.
 */
export type BedrockVersion = {
	/**
	 * The protocol version of the server.
	 */
	protocol: number;

	/**
	 * The version name of the server.
	 */
	name: string;
};

/**
 * The gamemode of a server.
 */
export type GameMode = {
	/**
	 * The name of this gamemode.
	 */
	name: string;

	/**
	 * The numeric of this gamemode, -1 if unknown.
	 */
	numericId: number;
};
