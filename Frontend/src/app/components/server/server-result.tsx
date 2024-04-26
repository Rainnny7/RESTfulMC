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
import { cn } from "@/app/common/utils";
import Image from "next/image";
import { ReactElement } from "react";
import {
    CachedBedrockMinecraftServer,
    CachedJavaMinecraftServer,
} from "restfulmc-lib";
import config from "@/config";
import { minecraft } from "@/font/fonts";
import CodeDialog from "@/components/code/code-dialog";
import RawJson from "@/components/badge/raw-json";

/**
 * The props for a server result.
 */
type ServerResultProps = {
    /**
     * The result of a search.
     */
    server: CachedJavaMinecraftServer | CachedBedrockMinecraftServer;
};

/**
 * The result of a server search.
 *
 * @param server the server to display
 * @returns the server result jsx
 */
const ServerResult = ({ server }: ServerResultProps): ReactElement => {
    const favicon: string | undefined = (server as CachedJavaMinecraftServer)
        .favicon?.url; // The favicon of the server
    return (
        <div className="flex flex-col gap-3">
            {/* Result */}
            <div
                className={cn(
                    "w-[37.5rem] relative p-1.5 flex gap-2 items-center pointer-events-none",
                    `bg-[url("/media/dirt-background.png")] bg-center bg-repeat ${minecraft.className}`
                )}
            >
                {/* Favicon */}
                <Image
                    className="h-16 w-16"
                    src={
                        favicon || `${config.apiEndpoint}/server/icon/fallback`
                    }
                    alt={`${server.hostname}'s Favicon`}
                    width={64}
                    height={64}
                />

                {/* Content */}
                <div className="-translate-y-1 w-full h-full flex flex-col gap-1">
                    {/* Name & Ping */}
                    <div className="w-full flex justify-between">
                        <h1>{server.hostname}</h1>

                        {/* Players & Ping */}
                        <div className="flex gap-1 items-center">
                            <p className="text-[#AAAAAA]">
                                {server.players.online}
                                <span className="text-[#555555]">/</span>
                                {server.players.max}
                            </p>
                            <Image
                                src="/media/ping-full.png"
                                alt="Ping!"
                                width={20}
                                height={20}
                            />
                        </div>
                    </div>

                    {/* MOTD */}
                    <div className="w-full flex flex-col">
                        {server.motd.html.map(
                            (line: string, index: number): ReactElement => {
                                return (
                                    <p
                                        key={index}
                                        className="leading-[1.15rem] pointer-events-auto"
                                        dangerouslySetInnerHTML={{
                                            __html: line,
                                        }}
                                    ></p>
                                );
                            }
                        )}
                    </div>
                </div>
            </div>

            {/* Raw Json */}
            <CodeDialog
                title="Raw Server Data"
                description={`The raw JSON data for the player ${server.hostname}:`}
                language="json"
                trigger={<RawJson />}
            >
                {JSON.stringify(server, undefined, 4)}
            </CodeDialog>
        </div>
    );
};
export default ServerResult;
