import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { shuffleArray } from "@/lib/array";
import { env } from "@/lib/env";
import Image from "next/image";
import { ReactElement, useMemo } from "react";

const EXAMPLE_SERVERS: Record<string, string> = {
    Hypixel: "hypixel.net",
    CubeCraft: "cubecraft.net",
    WildNetwork: "wildnetwork.net",
    Mineplex: "mineplex.com",
    Aetheria: "aetheria.cc",
    Wynncraft: "wynncraft.com",
    PurplePrison: "purpleprison.net",
    CosmicPvP: "cosmicpvp.com",
    Minehut: "minehut.com",
    "2b2t": "2b2t.org",
    Hoplite: "hoplite.gg",
    "Minmen Club": "minemen.club",
    "OG-Network": "og-network.net",
    GTM: "gtm.network",
};

const ServerExamples = (): ReactElement => {
    const displayedServers: [string, string][] = useMemo(
        () => shuffleArray(Object.entries(EXAMPLE_SERVERS)).slice(0, 5),
        []
    );
    return (
        <Card className="w-full max-w-xl bg-card/45 backdrop-blur-md">
            <CardHeader>
                <CardTitle>Server Examples</CardTitle>
            </CardHeader>
            <CardContent className="flex flex-wrap justify-center gap-1 items-center">
                {displayedServers.map(([name, server]) => (
                    <SimpleTooltip
                        key={server}
                        content={`Click to view ${name}'s Data`}
                        side="bottom"
                    >
                        <div className="pt-2 w-26 bg-card border border-border rounded-lg">
                            <SimpleLink
                                className="flex flex-col items-center"
                                href={`/server/java/${server}`}
                            >
                                <Image
                                    className="rounded-md"
                                    src={`${env.NEXT_PUBLIC_API_URL}/server/${server}/icon.png`}
                                    alt={`${name}'s Icon`}
                                    width={64}
                                    height={64}
                                    draggable={false}
                                    unoptimized
                                />
                                <Separator className="mt-2" />
                                <div className="p-1.5 w-full text-center font-semibold bg-muted/50">
                                    {name}
                                </div>
                            </SimpleLink>
                        </div>
                    </SimpleTooltip>
                ))}
            </CardContent>
            <CardFooter className="text-muted-foreground">
                Here are some example servers you can quickly view the data for.
            </CardFooter>
        </Card>
    );
};
export default ServerExamples;
