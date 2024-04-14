import { describe, it } from "bun:test";
import { getMinecraftServer, getPlayer } from "../src";
import { CachedPlayer } from "../src/types/player";
import { BedrockMinecraftServer } from "../src/types/server/bedrock-server";
import { JavaMinecraftServer } from "../src/types/server/java-server";

describe("player", () => {
	it("Rainnny", async () => {
		const player: CachedPlayer = await getPlayer("Rainnny");
		console.log("player found", player.cached);
	});
});

describe("server", () => {
	it("java", async () => {
		const server: JavaMinecraftServer | BedrockMinecraftServer =
			await getMinecraftServer("java", "play.wildnetwork.net");

		if ((server as BedrockMinecraftServer).id) {
		}
		console.log(server.ip);
	});
});
