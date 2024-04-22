"use client";

import MinecraftButton from "@/components/button/minecraft-button";
import { Input } from "@/components/ui/input";

import { Label } from "@/components/ui/label";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { capitalize } from "@/app/common/string-utils";
import { useRouter } from "next/navigation";
import { FormEvent, ReactElement, useState } from "react";
import { ServerPlatform } from "restfulmc-lib";
import SimpleTooltip from "@/components/simple-tooltip";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";
import { Loader2 } from "lucide-react";

/**
 * Props for a server search.
 */
type ServerSearchProps = {
    /**
     * The original platform to query for.
     */
    platform: string | undefined;

    /**
     * The original hostname to query for.
     */
    hostname: string | undefined;
};

/**
 * A component for searching for a server.
 *
 * @param platform the original platform to query for
 * @param hostname original hostname to query for
 * @returns the search component jsx
 */
const ServerSearch = ({
    platform,
    hostname,
}: ServerSearchProps): ReactElement => {
    const router: AppRouterInstance = useRouter();
    const [loading, setLoading] = useState<boolean>(false); // If the search is loading

    const handleRedirect = async (
        event: FormEvent<HTMLFormElement>
    ): Promise<void> => {
        setLoading(true); // Start loading
        event.preventDefault(); // Prevent the form from submitting

        // Get the form data
        const form: FormData = new FormData(event.currentTarget);
        router.push(`/server/${form.get("platform")}/${form.get("hostname")}`);
    };

    // Render the search form
    return (
        <form
            className="flex flex-col gap-7 justify-center items-center"
            onSubmit={handleRedirect}
        >
            {/* Input */}
            <div className="w-full flex gap-2">
                {/* Platform Selection */}
                <div className="flex flex-col gap-3">
                    <Label htmlFor="platform">Platform</Label>
                    <Select
                        name="platform"
                        defaultValue={platform || "java"}
                        required
                    >
                        <SelectTrigger className="w-28">
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            {Object.values(ServerPlatform).map(
                                (
                                    platform: ServerPlatform,
                                    index: number
                                ): ReactElement => (
                                    <SelectItem key={index} value={platform}>
                                        {capitalize(platform)}
                                    </SelectItem>
                                )
                            )}
                        </SelectContent>
                    </Select>
                </div>

                {/* Hostname Query */}
                <div className="w-full flex flex-col gap-3">
                    <Label htmlFor="hostname">Hostname</Label>
                    <Input
                        type="search"
                        name="hostname"
                        placeholder="Query..."
                        defaultValue={hostname}
                        required
                        maxLength={36}
                        autoComplete="off"
                    />
                </div>
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
export default ServerSearch;
