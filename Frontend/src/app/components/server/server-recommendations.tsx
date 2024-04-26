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
import { ReactElement } from "react";
import { cn } from "@/lib";
import { minecrafter } from "@/font/fonts";
import { ServerPlatform } from "restfulmc-lib";
import { Button } from "@/components/ui/button";
import Image from "next/image";
import SimpleTooltip from "@/components/simple-tooltip";
import { capitalize } from "@/lib/string-utils";
import SimpleLink from "@/components/simple-link";

/**
 * The recommendations for a server.
 */
const RECOMMENDATIONS: {
    [hostname: string]: ServerPlatform;
} = {
    "demo.restfulmc.cc": ServerPlatform.JAVA,
    "hypixel.net": ServerPlatform.JAVA,
    "cubecraft.net": ServerPlatform.JAVA,
    "wildprison.net": ServerPlatform.JAVA,
    "wildprison.bedrock.minehut.gg": ServerPlatform.BEDROCK,
    "play.lbsg.net": ServerPlatform.BEDROCK,
};

/**
 * The recommendations for
 * a server to test.
 *
 * @return the recommendations jsx
 */
const ServerRecommendations = (): ReactElement => (
    <div className="p-4 flex flex-col gap-2.5 items-center bg-muted rounded-xl">
        {/* Header */}
        <h1
            className={cn(
                "text-xl text-minecraft-green-3 select-none pointer-events-none",
                minecrafter.className
            )}
        >
            Try A Server
        </h1>

        {/* Recommendations */}
        <div className="max-w-2xl flex flex-wrap gap-2 justify-center">
            {Object.entries(RECOMMENDATIONS).map(
                (
                    [hostname, platform]: [string, ServerPlatform],
                    index: number
                ): ReactElement => (
                    <SimpleLink
                        key={index}
                        href={`/server/${platform}/${hostname}`}
                    >
                        <SimpleTooltip
                            content={`Click to test ${capitalize(platform)} server`}
                        >
                            <Button
                                className="px-10 flex gap-2.5 font-semibold hover:opacity-85 transition-all transform-gpu"
                                variant="outline"
                            >
                                <Image
                                    src={`/media/platform/${platform}.png`}
                                    alt=""
                                    width={26}
                                    height={26}
                                />
                                <span>{hostname}</span>
                            </Button>
                        </SimpleTooltip>
                    </SimpleLink>
                )
            )}
        </div>
    </div>
);
export default ServerRecommendations;
