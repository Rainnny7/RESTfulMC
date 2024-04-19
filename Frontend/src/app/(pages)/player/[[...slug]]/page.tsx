import Embed from "@/components/embed";
import PlayerResult from "@/components/player/player-result";
import PlayerSearch from "@/components/player/player-search";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import { PageProps } from "@/types/page";
import { ExclamationCircleIcon } from "@heroicons/react/24/outline";
import { Metadata, Viewport } from "next";
import { ReactElement } from "react";
import { CachedPlayer, getPlayer, type RestfulMCAPIError } from "restfulmc-lib";

/**
 * The page to lookup a player.
 *
 * @returns the page jsx
 */
const PlayerPage = async ({ params }: PageProps): Promise<ReactElement> => {
    let error: string | undefined = undefined; // The error to display
    let result: CachedPlayer | undefined = undefined; // The player to display
    const query: string | undefined = trimQuery(params.slug?.[0]); // The query to search for

    // Try and get the player to display
    try {
        result = query ? await getPlayer(query) : undefined;
    } catch (err) {
        error = (err as RestfulMCAPIError).message; // Set the error message
    }

    // Render the page
    return (
        <main className="px-3 h-screen flex justify-center items-center">
            <div className="flex flex-col gap-7">
                <h1
                    className={cn(
                        "mt-20 text-6xl text-minecraft-green-3 text-center pointer-events-none",
                        minecrafter.className
                    )}
                >
                    Player Lookup
                </h1>

                <div className="flex flex-col gap-5 px-10 xs:px-0">
                    {/* Error */}
                    {error && (
                        <Alert variant="destructive">
                            <ExclamationCircleIcon width={20} height={20} />
                            <AlertTitle>Error</AlertTitle>
                            <AlertDescription>{error}</AlertDescription>
                        </Alert>
                    )}

                    {/* Search */}
                    <PlayerSearch query={query} />
                </div>

                {/* Player Result */}
                {result && <PlayerResult query={query} player={result} />}
            </div>
        </main>
    );
};

/**
 * Generate metadata for this page.
 *
 * @param params the route params
 * @returns the generated metadata
 */
export const generateMetadata = async ({
    params,
}: PageProps): Promise<Metadata> => {
    const embed: Metadata | undefined = await getPageEmbed(
        trimQuery(params.slug?.[0])
    ); // Get the page embed

    // Return the page embed
    return embed
        ? embed
        : Embed({
              title: "Player Lookup",
              description: "Search for a player to view their profile.",
          });
};

/**
 * Generate the viewport for this page.
 *
 * @param params the route params
 * @returns the generated metadata
 */
export const generateViewport = async ({
    params,
}: PageProps): Promise<Viewport> => {
    const embed: Metadata | undefined = await getPageEmbed(
        trimQuery(params.slug?.[0])
    ); // Get the page embed
    return embed ? {} : { themeColor: "#FF5555" };
};

/**
 * Trim the given query.
 *
 * @param query the query to trim
 * @returns the trimmed query
 */
const trimQuery = (query: string | undefined): string | undefined => {
    // Limit the query to 36 chars
    if (query && query.length > 36) {
        query = query.substring(0, 36);
    }
    return query;
};

/**
 * Get the embed for this page.
 *
 * @param query the query to embed, if any
 * @returns the page embed
 */
const getPageEmbed = async (
    query: string | undefined
): Promise<Metadata | undefined> => {
    if (!query) {
        return undefined;
    }
    try {
        const player: CachedPlayer = await getPlayer(query); // Get the player to embed
        return Embed({
            title: `${player.username}'s Profile`,
            description: `UUID: ${player.uniqueId}\n\nClick to view data about this player.`,
            thumbnail: player.skin.parts.HEAD,
        });
    } catch (err) {
        const code: number = (err as RestfulMCAPIError).code; // Get the error status code
        if (code === 400) {
            return Embed({
                title: "Invalid Player",
                description: `The player ${query} is invalid.`,
            });
        } else if (code === 404) {
            return Embed({
                title: "Player Not Found",
                description: `The player ${query} was not found.`,
            });
        }
    }
};

export default PlayerPage;
