import MinecraftButton from "@/components/minecraft-button";
import { Input } from "@/components/ui/input";

import { redirect } from "next/navigation";

/**
 * A component for searching for a player.
 *
 * @param query the query to search for
 * @returns the search component jsx
 */
const PlayerSearch = ({
	query,
}: {
	query: string | undefined;
}): JSX.Element => {
	const handleRedirect = async (form: FormData) => {
		"use server";
		redirect(`/player/${form.get("query")}`);
	};
	return (
		<form
			className="flex flex-col gap-7 justify-center items-center"
			action={handleRedirect}
		>
			<Input
				name="query"
				placeholder="Username / UUID"
				defaultValue={query}
				maxLength={36}
			/>
			<MinecraftButton type="submit">Search</MinecraftButton>
		</form>
	);
};
export default PlayerSearch;
