import PlayerSearch from "@/components/player/player-search";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import { PageProps } from "@/types/page";
import { Metadata } from "next";
import Image from "next/image";
import { CachedPlayer, getPlayer, type RestfulMCAPIError } from "restfulmc-lib";
import PlayerResult from "../../../components/player/player-result";

/**
 * The page to lookup a player.
 *
 * @returns the page jsx
 */
const PlayerPage = async ({ params }: PageProps): Promise<JSX.Element> => {
	let error: string | undefined = undefined; // The error to display
	let result: CachedPlayer | undefined = undefined; // The player to display
	let query: string | undefined = params.slug?.[0]; // The query to search for

	// Limit the query to 36 chars
	if (query && query.length > 36) {
		query = query.substr(0, 36);
	}

	// Try and get the player to display
	try {
		result = params.slug ? await getPlayer(query) : undefined;
	} catch (err) {
		error = (err as RestfulMCAPIError).message; // Set the error message
	}

	// Render the page
	return (
		<main className="h-screen flex flex-col justify-center items-center">
			<div className="flex gap-32">
				{/* Header */}
				<Image
					className="my-auto h-[28rem] pointer-events-none"
					src="/media/players.webp"
					alt="Minecraft Players"
					width={632}
					height={632}
				/>

				<div className="flex flex-col gap-7">
					<h1
						className={cn(
							"mt-20 text-6xl text-minecraft-green-3 pointer-events-none",
							minecrafter.className
						)}
					>
						Player Lookup
					</h1>

					{/* Error */}
					{error && <p className="text-red-500">{error}</p>}

					{/* Search */}
					<PlayerSearch query={query} />

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
const generateMetadata = async ({ params }: PageProps): Promise<Metadata> => {
	console.log("params", params);
	return {
		title: "bob ross",
	};
};

export default PlayerPage;
