import { getMojangServerStatus } from "@/index";
import { MojangServerStatusResponse } from "@/types/mojang/server-status-response";
import { expect, test } from "bun:test";

/**
 * Run a test to ensure retrieving
 * the status of Mojang servers is
 * successful.
 */
test("ensureServerStatusCheckSuccess", async () => {
    const response: MojangServerStatusResponse = await getMojangServerStatus(); // Get Mojang service status
    expect(response).toBeDefined();
});
