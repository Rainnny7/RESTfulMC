import { cache } from "react";
import { getMinecraftServer, getPlayer, ServerPlatform } from "restfulmc-lib";

/**
 * Cached versions of API functions for request deduplication.
 * When called multiple times with the same args (e.g. in page + generateMetadata),
 * only one request is made.
 */
export const getCachedPlayer = cache(getPlayer);

export const getCachedMinecraftServer = cache(
    (platform: ServerPlatform, slug: string) =>
        getMinecraftServer(platform, slug)
);
