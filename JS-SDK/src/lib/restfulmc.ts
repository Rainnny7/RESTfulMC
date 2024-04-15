import { WebRequest } from "@/lib/webRequest";
import { MojangServerStatus } from "@/types/mojang";
import type { CachedPlayer } from "@/types/player";
import { Platform } from "@/types/server";
import { CachedBedrockMinecraftServer } from "@/types/server/bedrock-server";
import { CachedJavaMinecraftServer } from "@/types/server/java-server";

/**
 * Get a player by their username or UUID.
 *
 * @param query the query to search for the player by
 * @returns the promised player
 */
export const getPlayer = (query: string): Promise<CachedPlayer> =>
	new WebRequest(`/player/${query}`).execute<CachedPlayer>();

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
	platform === Platform.Java
		? new WebRequest(
				`/server/${platform}/${hostname}`
		  ).execute<CachedJavaMinecraftServer>()
		: new WebRequest(
				`/server/${platform}/${hostname}`
		  ).execute<CachedBedrockMinecraftServer>();

/**
 * Check if the server with the
 * given hostname is blocked by Mojang.
 *
 * @param hostname the server hostname to check
 * @returns the promised blocked status
 */
export const isMojangBlocked = (hostname: string): Promise<boolean> =>
	new WebRequest(`/server/blocked/${hostname}`).execute<boolean>();

/**
 * Get the status of Mojang servers.
 *
 * @returns the promised status
 */
export const getMojangServerStatus = (): Promise<MojangServerStatus> =>
	new WebRequest("/mojang/status").execute<MojangServerStatus>();
