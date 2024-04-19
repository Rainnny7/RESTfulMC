import MinecraftButton from "@/components/minecraft-button";
import { Input } from "@/components/ui/input";

import { Label } from "@/components/ui/label";
import { redirect } from "next/navigation";
import { ReactElement } from "react";

/**
 * Props for a player search.
 */
type PlayerSearchProps = {
    /**
     * The original query to search for.
     */
    query: string | undefined;
};

/**
 * A component for searching for a player.
 *
 * @param query the query to search for
 * @returns the search component jsx
 */
const PlayerSearch = ({ query }: PlayerSearchProps): ReactElement => {
    const handleRedirect = async (form: FormData): Promise<void> => {
        "use server";
        redirect(`/player/${form.get("query")}`);
    };
    return (
        <form
            className="flex flex-col gap-7 justify-center items-center"
            action={handleRedirect}
        >
            <div className="w-full flex flex-col gap-3">
                <Label htmlFor="query">Username or UUID</Label>
                <Input
                    type="search"
                    name="query"
                    placeholder="Query..."
                    defaultValue={query}
                    required
                    maxLength={36}
                    autoComplete="off"
                />
            </div>
            <MinecraftButton type="submit">Search</MinecraftButton>
        </form>
    );
};
export default PlayerSearch;
