import {WebRequest} from "@/lib/web-request";
import {HttpMethod} from "@/types/http-method";
import {MojangServerStatusResponse} from "@/types/mojang/server-status-response";
import {CachedPlayer} from "@/types/player/player";
import {SkinPart} from "@/types/player/skin-part";
import {CachedBedrockMinecraftServer} from "@/types/server/bedrock/server";
import {CachedJavaMinecraftServer} from "@/types/server/java-server";
import {ServerPlatform} from "@/types/server/platform";

/**
 * Get a player by their username or UUID.
 *
 * @param query the query to search for the player by
 * @returns the promised player
 */
export const getPlayer = (query: string): Promise<CachedPlayer> =>
	new WebRequest(HttpMethod.GET, `/player/${query}`).execute<CachedPlayer>();

/**
 * Get the part of a skin texture for
 * a player by their username or UUID.
 *
 * @param part the part of the player's skin to get
 * @param query the query to search for the player by
 * @param extension the skin part image extension
 * @param size the size of the skin part image
 * @returns the promised skin part texture
 */
export const getSkinPart = (
	part: SkinPart,
	query: string,
	extension: "png" | "jpg" = "png",
	size: number = 128
): Promise<ArrayBuffer> =>
	new WebRequest(
		HttpMethod.GET,
		`/player/${part}/${query}.${extension}?size=${size}`
	).execute<ArrayBuffer>();

/**
 * Get a Minecraft server by its platform and hostname.
 *
 * @param platform the platform of the server
 * @param hostname the hostname of the server
 * @returns the promised server
 */
export const getMinecraftServer = (
	platform: ServerPlatform,
	hostname: string
): Promise<CachedJavaMinecraftServer | CachedBedrockMinecraftServer> =>
	platform === ServerPlatform.JAVA
		? new WebRequest(
				HttpMethod.GET,
				`/server/${platform}/${hostname}`
		  ).execute<CachedJavaMinecraftServer>()
		: new WebRequest(
				HttpMethod.GET,
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
	new WebRequest(HttpMethod.GET, `/server/blocked/${hostname}`)
		.execute<{
			blocked: boolean;
		}>()
		.then((res) => res.blocked);

/**
 * Get the icon of the Java Minecraft
 * server with the given hostname.
 *
 * @param hostname the hostname of the server
 * @returns the primised server icon
 */
export const getJavaServerFavicon = (hostname: string): Promise<ArrayBuffer> =>
	new WebRequest(
		HttpMethod.GET,
		`/server/icon/${hostname}`
	).execute<ArrayBuffer>();

/**
 * Get the status of Mojang servers.
 *
 * @returns the promised status
 */
export const getMojangServerStatus = (): Promise<MojangServerStatusResponse> =>
	new WebRequest(
		HttpMethod.GET,
		"/mojang/status"
	).execute<MojangServerStatusResponse>();
