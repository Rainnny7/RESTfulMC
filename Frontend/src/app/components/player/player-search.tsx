"use client";

import MinecraftButton from "@/components/button/minecraft-button";
import { Input } from "@/components/ui/input";

import { Label } from "@/components/ui/label";
import { useRouter } from "next/navigation";
import { FormEvent, ReactElement, useState } from "react";
import SimpleTooltip from "@/components/simple-tooltip";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";
import { Loader2 } from "lucide-react";

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
 * @param query the original query to search for
 * @returns the search component jsx
 */
const PlayerSearch = ({ query }: PlayerSearchProps): ReactElement => {
    const router: AppRouterInstance = useRouter();
    const [loading, setLoading] = useState<boolean>(false); // If the search is loading

    const handleRedirect = async (
        event: FormEvent<HTMLFormElement>
    ): Promise<void> => {
        setLoading(true); // Start loading
        event.preventDefault(); // Prevent the form from submitting

        // Get the form data
        const form: FormData = new FormData(event.currentTarget);
        router.push(`/player/${form.get("query")}`);
    };

    // Render the search form
    return (
        <form
            className="flex flex-col gap-7 justify-center items-center"
            onSubmit={handleRedirect}
        >
            {/* Input */}
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

            {/* Search */}
            <SimpleTooltip content="Click to search">
                <MinecraftButton type="submit" disabled={loading}>
                    {loading && (
                        <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    )}
                    Search
                </MinecraftButton>
            </SimpleTooltip>
        </form>
    );
};
export default PlayerSearch;
