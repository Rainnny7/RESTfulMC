import { ErrorResponse } from "@/types/generic";

const API_ENDPOINT = "https://mc.rainnny.club"; // The API endpoint to use

/**
 * Make a web request to the API.
 */
export class WebRequest {
	/**
	 * The endpoint to make the request to.
	 */
	endpoint: string;

	constructor(endpoint: string) {
		this.endpoint = endpoint;
	}

	/**
	 * Execute this web request.
	 *
	 * @returns the promised response
	 * @template T the type of the response
	 */
	execute = <T>(): Promise<T> =>
		new Promise(async (resolve, reject) => {
			const response: Response = await fetch(
				`${API_ENDPOINT}/${this.endpoint}`
			); // Send the request
			const contentType: string | null = response.headers.get("Content-Type"); // Get the response content type

			// Parse as Json
			if (contentType === "application/json") {
				const json: any = await response.json();

				// Resolve the response
				if (response.ok) {
					resolve(json as T);
				} else {
					reject(json as ErrorResponse); // The request failed
				}
			} else {
				// Fallback to an array buffer
				resolve((await response.arrayBuffer()) as T);
			}
		});
}
