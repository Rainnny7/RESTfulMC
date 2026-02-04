import { WebRequest } from "@/lib/web-request";
import { HttpMethod } from "@/types/http-method";
import { MojangServerStatusResponse } from "@/types/mojang/server-status-response";
import { CachedPlayer } from "@/types/player/player";
import { SkinPart } from "@/types/player/skin-part";
import { CachedBedrockMinecraftServer } from "@/types/server/bedrock/server";
import { CachedJavaMinecraftServer } from "@/types/server/java-server";
import { ServerPlatform } from "@/types/server/platform";

/**
 * Get a player by their username or UUID.
 *
 * @param query the query to search for the player by
 * @param signed whether the profile is signed by Mojang
 * @returns the promised player
 */
export const getPlayer = (
    query: string,
    signed: boolean = false
): Promise<CachedPlayer> =>
    new WebRequest(
        HttpMethod.GET,
        `/player/${query}${signed ? "?signed=true" : ""}`
    ).execute<CachedPlayer>();

/**
 * Get the part of a skin texture for
 * a player by their username or UUID.
 *
 * @param part the part of the player's skin to get
 * @param query the query to search for the player by
 * @param extension the skin part image extension
 * @param overlays whether to render skin overlays
 * @param size the size of the skin part image
 * @returns the promised skin part texture
 */
export const getSkinPart = (
    part: SkinPart,
    query: string,
    extension: "png" | "jpg" = "png",
    overlays: boolean = true,
    size: number = 512
): Promise<ArrayBuffer> =>
    new WebRequest(
        HttpMethod.GET,
        `/player/${query}/${part}.${extension}?overlays=${overlays}&size=${size}`
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
 * @returns the promised server icon
 */
export const getJavaServerFavicon = (hostname: string): Promise<ArrayBuffer> =>
    new WebRequest(
        HttpMethod.GET,
        `/server/${hostname}/icon.png`
    ).execute<ArrayBuffer>();

/**
 * Get the MOTD of a Minecraft server
 * as a PNG image.
 *
 * @param platform the platform of the server
 * @param hostname the hostname of the server
 * @param size the size to scale the MOTD texture to (default 768)
 * @returns the promised MOTD PNG image
 */
export const getServerMotd = (
    platform: ServerPlatform,
    hostname: string,
    size: number = 768
): Promise<ArrayBuffer> =>
    new WebRequest(
        HttpMethod.GET,
        `/server/${platform}/${hostname}/motd.png?size=${size}`
    ).execute<ArrayBuffer>();

/**
 * Get the MOTD of a Minecraft server
 * as HTML.
 *
 * @param platform the platform of the server
 * @param hostname the hostname of the server
 * @returns the promised MOTD HTML
 */
export const getServerMotdHtml = (
    platform: ServerPlatform,
    hostname: string
): Promise<string> =>
    new WebRequest(
        HttpMethod.GET,
        `/server/${platform}/${hostname}/motd.html`
    ).execute<string>();

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
