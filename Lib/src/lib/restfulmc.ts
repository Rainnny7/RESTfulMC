import { ErrorResponse } from "../types/generic";
import type { CachedPlayer } from "../types/player";
import { Platform } from "../types/server";
import { CachedBedrockMinecraftServer } from "../types/server/bedrock-server";
import { CachedJavaMinecraftServer } from "../types/server/java-server";

const ENDPOINT = "https://mc.rainnny.club"; // The API endpoint to use

/**
 * Get a player by their username or UUID.
 *
 * @param query the query to search for the player by
 * @returns the promised player
 */
export const getPlayer = (query: string): Promise<CachedPlayer> => {
	return new Promise(async (resolve, reject) => {
		const response: Response = await fetch(`${ENDPOINT}/player/${query}`); // Request the player
		const json: any = await response.json();

		// Resolve the player
		if (response.ok) {
			resolve(json as CachedPlayer);
		} else {
			reject(json as ErrorResponse); // The request failed
		}
	});
};

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
): Promise<CachedJavaMinecraftServer | CachedBedrockMinecraftServer> => {
	return new Promise(async (resolve, reject) => {
		const response: Response = await fetch(
			`${ENDPOINT}/server/${platform}/${hostname}`
		); // Request the server
		const json: any = await response.json();

		// Resolve the server
		if (response.ok) {
			resolve(
				platform === "java"
					? (json as CachedJavaMinecraftServer)
					: (json as CachedBedrockMinecraftServer)
			);
		} else {
			reject(json as ErrorResponse); // The request failed
		}
	});
};

/**
 * Check if the server with the
 * given hostname is blocked by Mojang.
 *
 * @param hostname the server hostname to check
 * @returns the promised blocked status
 */
export const isMojangBlocked = (hostname: string): Promise<boolean> => {
	return new Promise(async (resolve, reject) => {
		const response: Response = await fetch(
			`${ENDPOINT}/server/blocked/${hostname}`
		); // Request the server's blocked status
		const json: any = await response.json();

		// Resolve the blocked status
		if (response.ok) {
			resolve(json.blocked as boolean);
		} else {
			reject(json as ErrorResponse); // The request failed
		}
	});
};

/**
 * Get the status of Mojang servers.
 *
 * @returns the promised status
 */
export const getMojangServerStatus = (): Promise<MojangServerStatus> => {
	return new Promise(async (resolve, reject) => {
		const response: Response = await fetch(`${ENDPOINT}/mojang/status`); // Request the statuses
		const json: any = await response.json();

		// Resolve the blocked status
		if (response.ok) {
			resolve(json as MojangServerStatus);
		} else {
			reject(json as ErrorResponse); // The request failed
		}
	});
};
