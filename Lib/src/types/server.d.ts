/**
 * A model representing a Minecraft server.
 */
type MinecraftServer = {
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
	records: DNSRecord[];

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
 * A representation of a DNS record.
 */
type DNSRecord = {
	/**
	 * The type of this record.
	 */
	type: RecordType;

	/**
	 * The TTL (Time To Live) of this record.
	 */
	ttl: number;
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

/**
 * A platform a Minecraft
 * server can operate on.
 */
export type Platform = "java" | "bedrock";

/**
 * Types of a DNS record.
 */
type RecordType = "A" | "SRV";
