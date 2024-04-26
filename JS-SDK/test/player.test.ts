import {getPlayer, getSkinPart} from "@/index";
import {RestfulMCAPIError} from "@/types/error";
import {CachedPlayer} from "@/types/player/player";
import {SkinPart} from "@/types/player/skin-part";
import {expect, test} from "bun:test";

/**
 * Run a test to ensure retrieving
 * a player's data is successful.
 */
test("ensurePlayerLookupSuccess", async () => {
	const player: CachedPlayer = await getPlayer("Rainnny"); // Fetch the player
	expect(player.username).toBe("Rainnny");
});

/**
 * Run a test to ensure retrieving an
 * invalid player results in a 404.
 */
test("ensurePlayerNotFound", async () => {
	try {
		await getPlayer("SDFSDFSDFSDFDDDG"); // Fetch the unknown player
	} catch (err) {
		expect((err as RestfulMCAPIError).code).toBe(404);
	}
});

/**
 * Run a test to ensure retrieving a player
 * with an invalid username results in a 400.
 */
test("ensureUsernameIsInvalid", async () => {
	try {
		await getPlayer("A"); // Fetch the invalid player
	} catch (err) {
		expect((err as RestfulMCAPIError).code).toBe(400);
	}
});

/**
 * Run a test to ensure retrieving a
 * player's skin part texture is successful.
 */
test("ensureSkinPartTextureSuccess", async () => {
	const partTexture: ArrayBuffer = await getSkinPart(SkinPart.HEAD, "Rainnny"); // Fetch the skin part
	expect(partTexture.byteLength).toBeGreaterThan(0);
});
