/**
 * The github star count component.
 *
 * @returns the star count jsx
 */
const GitHubStarCount = async (): Promise<JSX.Element> => {
	const stars: number = await getStarCount(); // Get the repo star count
	return (
		<code className="px-1 rounded-md bg-minecraft-green-3/80">{stars}</code>
	);
};

/**
 * Get the amount of stars
 * the repository has.
 *
 * @returns the star count
 */
const getStarCount = async (): Promise<number> => {
	const response: Response = await fetch(
		"https://api.github.com/repos/Rainnny7/RESTfulMC",
		{ next: { revalidate: 300 } } // Revalidate every 5 minutes
	);
	const json: any = await response.json(); // Get the JSON response
	return json.stargazers_count; // Return the stars
};

export default GitHubStarCount;
