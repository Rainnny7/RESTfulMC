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
import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import { Metadata } from "next";
import { ReactElement } from "react";
import {
    getMojangServerStatus,
    MojangServer,
    MojangServerStatus,
    MojangServerStatusResponse,
} from "restfulmc-lib";
import { ArrowTopRightOnSquareIcon } from "@heroicons/react/24/outline";
import SimpleTooltip from "@/components/simple-tooltip";
import SimpleLink from "@/components/simple-link";

/**
 * Page metadata.
 */
export const metadata: Metadata = {
    title: "Mojang Status",
    description: "View the status of Mojang servers.",
};

/**
 * Force the page to be dynamic, so
 * it will be regenerated on every request
 */
export const dynamic = "force-dynamic";

/**
 * The page to view the
 * status of Mojang servers.
 *
 * @returns the page jsx
 */
const MojangStatusPage = async (): Promise<ReactElement> => {
    const { servers }: MojangServerStatusResponse =
        await getMojangServerStatus(); // Get Mojang server statuses
    return (
        <main className="mb-32 xl:mb-24 2xl:mb-12 h-screen flex flex-col gap-7 justify-center items-center transition-all transform-gpu">
            {/* Header */}
            <h1
                className={cn(
                    "mt-20 text-6xl text-minecraft-green-3 text-center select-none pointer-events-none",
                    minecrafter.className
                )}
            >
                Mojang Status
            </h1>

            {/* Server Statuses */}
            <div className="w-[21.5rem] xs:w-[29rem] sm:w-[33.5rem] px-5 py-3.5 flex flex-col gap-2.5 bg-muted rounded-xl divide-y-2 divide-zinc-700/40 transition-all transform-gpu">
                {servers.map(
                    (server: MojangServer, index: number): ReactElement => {
                        const status: MojangServerStatus = server.status; // The status of the server
                        return (
                            <div
                                key={index}
                                className={cn(
                                    "flex justify-between items-center",
                                    index > 0 && "pt-2"
                                )}
                            >
                                <div className="flex flex-col gap-0.5">
                                    <h1 className="pointer-events-none">
                                        {server.name}
                                    </h1>
                                    <SimpleTooltip content="Click to open">
                                        <SimpleLink
                                            href={server.endpoint}
                                            className="text-xs text-minecraft-green-3 hover:opacity-85 transition-all transform-gpu"
                                            noRef
                                            newTab
                                        >
                                            <code className="flex gap-1 items-center">
                                                <span>{server.endpoint}</span>
                                                <ArrowTopRightOnSquareIcon
                                                    width={16}
                                                    height={16}
                                                />
                                            </code>
                                        </SimpleLink>
                                    </SimpleTooltip>
                                </div>

                                {/* Status */}
                                <h2
                                    className={cn(
                                        "font-semibold pointer-events-none",
                                        statusStyles[status]
                                    )}
                                >
                                    {status}
                                </h2>
                            </div>
                        );
                    }
                )}
            </div>
        </main>
    );
};

/**
 * The styles for each status.
 */
const statusStyles: {
    [status: string]: string;
} = {
    ONLINE: "text-green-500",
    DEGRADED: "text-yellow-500",
    OFFLINE: "text-red-500",
};

export default MojangStatusPage;
