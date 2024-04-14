import { MojangServerStatus } from "../types/mojang";
import type { CachedPlayer } from "../types/player";
import { Platform } from "../types/server";
import { CachedBedrockMinecraftServer } from "../types/server/bedrock-server";
import { CachedJavaMinecraftServer } from "../types/server/java-server";
import { makeWebRequest } from "./webRequest";

/**
 * Get a player by their username or UUID.
 *
 * @param query the query to search for the player by
 * @returns the promised player
 */
export const getPlayer = (query: string): Promise<CachedPlayer> =>
	makeWebRequest<CachedPlayer>(`/player/${query}`);

/**
 * Get a Minecraft server by its platform and hostname.
 *
 * @param platform the platform of the server
 * @param hostname the hostname of the server
 * @returns the promised server
 */
export const getMinecraftServer = (
	platform: Platform,
	hostname: string
): Promise<CachedJavaMinecraftServer | CachedBedrockMinecraftServer> =>
	platform === "java"
		? makeWebRequest<CachedJavaMinecraftServer>(
				`/server/${platform}/${hostname}`
		  )
		: makeWebRequest<CachedBedrockMinecraftServer>(
				`/server/${platform}/${hostname}`
		  );

/**
 * Check if the server with the
 * given hostname is blocked by Mojang.
 *
 * @param hostname the server hostname to check
 * @returns the promised blocked status
 */
export const isMojangBlocked = (hostname: string): Promise<boolean> =>
	makeWebRequest<boolean>(`/server/blocked/${hostname}`);

/**
 * Get the status of Mojang servers.
 *
 * @returns the promised status
 */
export const getMojangServerStatus = (): Promise<MojangServerStatus> =>
	makeWebRequest<MojangServerStatus>("/mojang/status");
