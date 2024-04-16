import { RestfulMCAPIError } from "@/types/error";
import { HttpMethod } from "@/types/http-method";

const API_ENDPOINT = "https://api.restfulmc.cc"; // The API endpoint to use

/**
 * Make a web request to the API.
 */
export class WebRequest {
	/**
	 * The HTTP method to use.
	 */
	method: HttpMethod;

	/**
	 * The endpoint to make the request to.
	 */
	endpoint: string;

	constructor(method: HttpMethod, endpoint: string) {
		this.method = method;
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
			const response: Response = await fetch(`${API_ENDPOINT}${this.endpoint}`); // Send the request
			const contentType: string | null = response.headers.get("Content-Type"); // Get the response content type

			// Parse as Json
			if (contentType === "application/json") {
				const json: any = await response.json();

				// Resolve the response
				if (response.ok) {
					resolve(json as T);
				} else {
					reject(json as RestfulMCAPIError); // The request failed
				}
			} else {
				// Fallback to an array buffer
				resolve((await response.arrayBuffer()) as T);
			}
		});
}
