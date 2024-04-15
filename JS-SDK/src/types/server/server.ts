import { ARecord, SRVRecord } from "@/types/dns/dns-record";

/**
 * A model representing a Minecraft server.
 */
export type MinecraftServer = {
	/**
	 * The hostname of this server.
	 */
	hostname: string;

	/**
	 * The IP address of this server, if resolved.
	 */
	ip?: string | undefined;

	/**
	 * The port of this server.
	 */
	port: number;

	/**
	 * The DNS records resolved for this server.
	 */
	records: ARecord | SRVRecord[];

	/**
	 * The player counts of this server.
	 */
	players: Players;

	/**
	 * The MOTD of this server.
	 */
	motd: MOTD;
};

/**
 * Player count data for a server.
 */
type Players = {
	/**
	 * The online players on this server.
	 */
	online: number;

	/**
	 * The maximum allowed players on this server.
	 */
	max: number;

	/**
	 * A sample of players on this server, undefined if no sample.
	 */
	sample?: PlayerSample[] | undefined;
};

/**
 * A sample player.
 */
type PlayerSample = {
	/**
	 * The ID of this player.
	 */
	id: string;

	/**
	 * The name of this player.
	 */
	name: PlayerSampleName;
};

/**
 * The name of a sample player.
 */
type PlayerSampleName = {
	/**
	 * The raw name.
	 */
	raw: string;

	/**
	 * The clean name (no color codes).
	 */
	clean: string;

	/**
	 * The HTML name.
	 */
	html: string;
};

/**
 * The MOTD for a server.
 */
type MOTD = {
	/**
	 * The raw MOTD lines.
	 */
	raw: string[];

	/**
	 * The clean MOTD lines (no color codes).
	 */
	clean: string[];

	/**
	 * The HTML MOTD lines.
	 */
	html: string[];
};
