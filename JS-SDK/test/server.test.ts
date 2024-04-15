import {
	ServerPlatform,
	getJavaServerFavicon,
	getMinecraftServer,
	isMojangBlocked,
} from "@/index";
import { RestfulMCAPIError } from "@/types/error";
import { CachedBedrockMinecraftServer } from "@/types/server/bedrock/server";
import { CachedJavaMinecraftServer } from "@/types/server/java-server";
import { expect, test } from "bun:test";

/**
 * Run a test to ensure retrieving a
 * Java server's status is successful.
 */
test("ensureJavaServerLookupSuccess", async () => {
	const server: CachedJavaMinecraftServer = (await getMinecraftServer(
		ServerPlatform.JAVA,
		"hypixel.net"
	)) as CachedJavaMinecraftServer; // Fetch the Java server
	expect(server.hostname).toBe("mc.hypixel.net");
});

/**
 * Run a test to ensure retrieving a
 * Bedrock server's status is successful.
 */
test("ensureBedrockServerLookupSuccess", async () => {
	const server: CachedBedrockMinecraftServer = (await getMinecraftServer(
		ServerPlatform.BEDROCK,
		"wildprison.bedrock.minehut.gg"
	)) as CachedBedrockMinecraftServer; // Fetch the Bedrock server
	expect(server.hostname).toBe("wildprison.bedrock.minehut.gg");
});

/**
 * Run a test to ensure looking up an
 * invalid hostname results in a 400.
 */
test("ensureUnknownHostname", async () => {
	try {
		await getMinecraftServer(ServerPlatform.JAVA, "invalid"); // Fetch the unknown server
	} catch (err) {
		expect((err as RestfulMCAPIError).code).toBe(400);
	}
});

/**
 * Run a test to ensure looking up an
 * invalid port results in a 400.
 */
test("ensureUnknownPort", async () => {
	try {
		await getMinecraftServer(ServerPlatform.JAVA, "hypixel.net:A"); // Fetch the invalid server
	} catch (err) {
		expect((err as RestfulMCAPIError).code).toBe(400);
	}
});

/**
 * Run a test to ensure checking if
 * a server is blocked is successful.
 */
test("ensureServerBanCheckSuccess", async () => {
	const blocked: boolean = await isMojangBlocked("arkhamnetwork.org"); // Check if the server is blocked
	expect(blocked).toBe(true);
});

/**
 * Run a test to ensure retrieving a
 * Java server's favicon is successful.
 */
test("ensureServerFaviconSuccess", async () => {
	const faviconTexture: ArrayBuffer = await getJavaServerFavicon("hypixel.net"); // Get the Java server's favicon
	expect(faviconTexture.byteLength).toBeGreaterThan(0);
});
