import { getMojangServerStatus } from "@/index";
import { MojangServerStatus } from "@/types/mojang";
import { expect, test } from "bun:test";

/**
 * Run a test to ensure retrieving
 * the status of Mojang servers is
 * successful.
 */
test("ensureServerStatusCheckSuccess", async () => {
	const status: MojangServerStatus = await getMojangServerStatus(); // Get Mojang service status
	expect(status).toBeDefined();
});
