/**
 * Represents the status of
 * a service provided by Mojang.
 */
export type MojangServerStatusResponse = {
	[endpoint: string]: Status;
};
