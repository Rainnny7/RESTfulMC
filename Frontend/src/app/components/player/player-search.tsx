import MinecraftButton from "@/components/minecraft-button";
import { Input } from "@/components/ui/input";

import { Label } from "@/components/ui/label";
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
			<div className="w-full flex flex-col gap-3">
				<Label>Username or UUID</Label>
				<Input
					type="search"
					name="query"
					placeholder="Query..."
					defaultValue={query}
					maxLength={36}
				/>
			</div>
			<MinecraftButton type="submit">Search</MinecraftButton>
		</form>
	);
};
export default PlayerSearch;
