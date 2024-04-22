import { ReactElement } from "react";
import { cn } from "@/lib";
import { minecrafter } from "@/font/fonts";
import { ServerPlatform } from "restfulmc-lib";
import { Button } from "@/components/ui/button";
import Image from "next/image";
import Link from "next/link";
import SimpleTooltip from "@/components/simple-tooltip";
import { capitalize } from "@/lib/string-utils";

/**
 * The recommendations for a server.
 */
const RECOMMENDATIONS: {
    [hostname: string]: ServerPlatform;
} = {
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
                    <Link key={index} href={`/server/${platform}/${hostname}`}>
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
                    </Link>
                )
            )}
        </div>
    </div>
);
export default ServerRecommendations;
