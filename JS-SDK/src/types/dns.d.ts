/**
 * An A record.
 */
export interface ARecord extends DNSRecord {
	/**
	 * The address of this record, undefined if unresolved.
	 */
	address?: string | undefined;
}

/**
 * An SRV record.
 */
export interface SRVRecord extends DNSRecord {
	/**
	 * The priority of this record.
	 */
	priority: number;

	/**
	 * The weight of this record.
	 */
	weight: number;

	/**
	 * The port of this record.
	 */
	port: number;

	/**
	 * The target of this record.
	 */
	target: string;
}

/**
 * A representation of a DNS record.
 */
export type DNSRecord = {
	/**
	 * The type of this record.
	 */
	type: RecordType;

	/**
	 * The TTL (Time To Live) of this record.
	 */
	ttl: number;
};
