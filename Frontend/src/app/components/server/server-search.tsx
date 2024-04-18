import MinecraftButton from "@/components/minecraft-button";
import { Input } from "@/components/ui/input";

import { Label } from "@/components/ui/label";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { redirect } from "next/navigation";
import { ReactElement } from "react";
import { ServerPlatform } from "restfulmc-lib";

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
 * @param query the query to search for
 * @returns the search component jsx
 */
const ServerSearch = ({
    platform,
    hostname,
}: ServerSearchProps): ReactElement => {
    const handleRedirect = async (form: FormData): Promise<void> => {
        "use server";
        redirect(`/server/${form.get("platform")}/${form.get("hostname")}`);
    };
    return (
        <form
            className="flex flex-col gap-7 items-center"
            action={handleRedirect}
        >
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
                                (platform, index) => (
                                    <SelectItem key={index} value={platform}>
                                        {platform.charAt(0).toUpperCase() +
                                            platform.substring(1)}
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
                    />
                </div>
            </div>
            <MinecraftButton type="submit">Search</MinecraftButton>
        </form>
    );
};
export default ServerSearch;
