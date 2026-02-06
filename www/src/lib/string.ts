/**
 * Determines if the input is a server address (domain or IP) vs a player identifier.
 * Server lookup: mineplex.com, mineplex.com:25565, 192.168.1.1, [::1]:25565
 * Player lookup: username, UUID, etc.
 */
export const isServerAddress = (input: string): boolean => {
    const trimmed = input.trim();
    if (!trimmed) return false;

    // IPv4 with optional port: 192.168.1.1 or 192.168.1.1:25565
    const ipv4Regex = /^(\d{1,3}\.){3}\d{1,3}(:\d{1,5})?$/;
    if (ipv4Regex.test(trimmed)) return true;

    // IPv6 with optional port: [::1] or [::1]:25565
    const ipv6Regex = /^\[[0-9a-fA-F:.]+\](:\d{1,5})?$/;
    if (ipv6Regex.test(trimmed)) return true;

    // Domain with optional port: mineplex.com or mineplex.com:25565
    // Matches hostname.tld, subdomain.hostname.tld
    const domainRegex =
        /^[a-zA-Z0-9]([a-zA-Z0-9.-]*[a-zA-Z0-9])?\.[a-zA-Z]{2,}(:\d{1,5})?$/;
    if (domainRegex.test(trimmed)) return true;

    // localhost with optional port
    if (/^localhost(:\d{1,5})?$/i.test(trimmed)) return true;

    return false;
};
