import { ErrorResponse } from "../types/generic";
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
		const json: any = await response.json();

		// Resolve the player
		if (response.ok) {
			resolve(json as Player);
		} else {
			reject(json as ErrorResponse); // The request failed
		}
	});
};
