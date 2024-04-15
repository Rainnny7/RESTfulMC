import { describe, it } from "bun:test";
import { getMinecraftServer, getMojangServerStatus, getPlayer } from "../src";
import { ErrorResponse } from "../src/types/generic";
import { MojangServerStatus } from "../src/types/mojang";
import { CachedPlayer } from "../src/types/player";
import { CachedBedrockMinecraftServer } from "../src/types/server/bedrock-server";
import { CachedJavaMinecraftServer } from "../src/types/server/java-server";

describe("player", () => {
	it("Rainnny", async () => {
		try {
			const player: CachedPlayer = await getPlayer("Rainnny");
			console.log(`Hello ${player.username}, your UUID is ${player.uniqueId}!`);
		} catch (err) {
			if ((err as ErrorResponse).code == 404) {
				console.error("Player with UUID or username not found.");
			}
		}
	});
});

describe("server", () => {
	it("java", async () => {
		const server: CachedJavaMinecraftServer | CachedBedrockMinecraftServer =
			await getMinecraftServer("java", "play.wildnetwork.net");
		console.log(server.ip);
	});
});

describe("mojang", () => {
	it("status", async () => {
		const status: MojangServerStatus = await getMojangServerStatus();
		console.log(status["https://sessionserver.mojang.com"]);
	});
});
