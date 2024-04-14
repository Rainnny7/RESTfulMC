import { ErrorResponse } from "../types/generic";

const ENDPOINT = "https://mc.rainnny.club"; // The API endpoint to use

/**
 * Make a web request to the API.
 *
 * @param url the endpoint to make the request to
 * @returns the promised response
 */
export const makeWebRequest = <T>(endpoint: string): Promise<T> =>
	new Promise(async (resolve, reject) => {
		const response: Response = await fetch(`${ENDPOINT}/${endpoint}`); // Request the player
		const json: any = await response.json();

		// Resolve the response
		if (response.ok) {
			resolve(json as T);
		} else {
			reject(json as ErrorResponse); // The request failed
		}
	});
