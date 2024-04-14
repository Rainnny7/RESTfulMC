import type { Player } from "../types/player";

const ENDPOINT = "https://mc.rainnny.club"; // The API endpoint to use

/**
 * Get a player by their username or UUID.
 *
 * @param query the query to search for the player by
 * @returns the promised player
 */
export const getPlayer = (query: string): Promise<Player> => {
	return new Promise(async (resolve, reject) => {
		const response: Response = await fetch(`${ENDPOINT}/player/${query}`); // Request the player
		resolve((await response.json()) as Player); // Resolve the player
	});
};
