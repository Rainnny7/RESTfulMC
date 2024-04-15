/**
 * Represents the status of
 * a service provided by Mojang.
 */
export type MojangServerStatus = {
	[endpoint: string]: Status;
};

/**
 * The status of a service.
 */
enum Status {
	ONLINE,
	DEGRADED,
	OFFLINE,
}
