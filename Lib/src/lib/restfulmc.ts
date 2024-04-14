import type { Player } from "../types/player";

/**
 * Get a player by their username or UUID.
 *
 * @param query the query to search for the player by
 * @returns the promised player
 */
export const getPlayer = (query: string): Promise<Player> => {
	return new Promise((resolve, reject) => {
		resolve({
			uniqueId: "fc1d5fe7-f29b-430d-80bb-3b093a638b0f",
			username: "Rainnny",
			skin: {
				url: "",
				model: "default",
				legacy: false,
			},
		});
	});
};
