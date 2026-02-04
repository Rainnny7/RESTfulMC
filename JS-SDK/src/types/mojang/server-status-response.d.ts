import { MojangServerStatus } from "@/types/mojang/server-status";

/**
 * Represents the status of
 * a service provided by Mojang.
 */
export type MojangServerStatusResponse = {
    /**
     * A list of Mojang servers.
     */
    servers: MojangServer[];
};

/**
 * A Mojang server.
 */
type MojangServer = {
    /**
     * The name of this server.
     */
    name: string;

    /**
     * The endpoint to this server.
     */
    endpoint: string;

    /**
     * The status of this server.
     */
    status: MojangServerStatus;
};
