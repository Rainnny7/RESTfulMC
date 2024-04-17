import Embed from "@/components/embed";
import PlayerResult from "@/components/player/player-result";
import PlayerSearch from "@/components/player/player-search";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import { PageProps } from "@/types/page";
import { Metadata } from "next";
import Image from "next/image";
import { CachedPlayer, getPlayer, type RestfulMCAPIError } from "restfulmc-lib";
import { ReactElement } from "react";

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
            <div className="mt-0 sm:mt-[45rem] xl:mt-0 flex flex-col xl:flex-row xl:gap-24 2xl:gap-48 transition-all transform-gpu">
                {/* Banner */}
                <Image
                    className="hidden sm:flex xl:my-auto h-[28rem] pointer-events-none"
                    src="/media/players.webp"
                    alt="Minecraft Players"
                    width={632}
                    height={632}
                />

                {/* Search */}
                <div className="pb-16 xl:pb-0 flex flex-col gap-7">
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
                        {error && <p className="text-red-500">{error}</p>}

                        {/* Search */}
                        <PlayerSearch query={query} />
                    </div>

                    {/* Player Result */}
                    {result && <PlayerResult player={result} />}
                </div>
            </div>
        </main>
    );
};

/**
 * Generate metadata for this page.
 *
 * @param params the route params
 * @param searchParams the search params
 * @returns the generated metadata
 */
export const generateMetadata = async ({
    params,
}: PageProps): Promise<Metadata> => {
    const query: string | undefined = trimQuery(params.slug?.[0]); // The query to embed for

    // Try and get the player to display
    if (query) {
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
                    description: "The player you searched for is invalid.",
                });
            } else if (code === 404) {
                return Embed({
                    title: "Player Not Found",
                    description: "The player you searched for was not found.",
                });
            }
        }
    }
    return {};
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
        query = query.substr(0, 36);
    }
    return query;
};

export default PlayerPage;
