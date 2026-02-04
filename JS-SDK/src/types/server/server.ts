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
     * The ASN data of this server, undefined if unknown.
     */
    asn?: AsnData | undefined;

    /**
     * The Geo location of this server, undefined if unknown.
     */
    geo?: GeoLocation | undefined;

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
 * The Geo location of a server.
 */
type GeoLocation = {
    /**
     * The continent of this server.
     */
    continent: LocationData;

    /**
     * The country of this server.
     */
    country: LocationData;

    /**
     * The city of this server, undefined if unknown.
     */
    city?: string | undefined;

    /**
     * The region of this server, undefined if unknown.
     */
    region?: string | undefined;

    /**
     * The IANA time zone (e.g. America/Toronto) of this server, undefined if unknown.
     */
    timezone?: string | undefined;

    /**
     * The postal code of this server, undefined if unknown.
     */
    postal?: string | undefined;

    /**
     * A quick link to the flag for the country.
     */
    flag: string;

    /**
     * The latitude of this server.
     */
    latitude: number;

    /**
     * The longitude of this server.
     */
    longitude: number;
};

/**
 * Data for a location.
 */
type LocationData = {
    /**
     * The location code.
     */
    code: string;

    /**
     * The location name.
     */
    name: string;
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
     * The unique id of this player.
     */
    id: string;

    /**
     * The name of this player.
     */
    name: PlayerSampleName;

    /**
     * The href to view player data for this player sample.
     */
    url: string;
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

    /**
     * The URL to preview this MOTD as PNG.
     */
    url: string;

    /**
     * The URL to preview this MOTD as HTML.
     */
    htmlUrl: string;
};

/**
 * The ASN data of a server.
 */
type AsnData = {
    /**
     * The ASN number.
     */
    number: number;

    /**
     * The ASN organization.
     */
    organization: string;

    /**
     * The CIDR range.
     */
    cidr: string;
};
