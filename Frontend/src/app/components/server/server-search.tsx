/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
