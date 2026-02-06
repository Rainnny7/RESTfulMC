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

const EXAMPLE_PLAYERS: string[] = [
    "Steve",
    "Alex",
    "Herobrine",
    "Dinnerbone",
    "Grumm",
    "Rainnny",
    "NoneTaken",
    "jeb_",
    "Notch",
    "ManlyWoman",
    "hypixel",
    "Technoblade",
    "DanTDM",
    "Dream",
];

const PlayerExamples = (): ReactElement => {
    const displayedPlayers: string[] = useMemo(
        () => shuffleArray(EXAMPLE_PLAYERS).slice(0, 5),
        []
    );
    return (
        <Card className="w-full max-w-xl bg-card/45 backdrop-blur-md">
            <CardHeader>
                <CardTitle>Player Examples</CardTitle>
            </CardHeader>
            <CardContent className="flex flex-wrap justify-center gap-1 items-center">
                {displayedPlayers.map((player) => (
                    <SimpleTooltip
                        key={player}
                        content={`Click to view ${player}'s Profile`}
                        side="bottom"
                    >
                        <div className="pt-2 w-26 bg-card border border-border rounded-lg">
                            <SimpleLink
                                className="flex flex-col items-center"
                                href={`/player/${player}`}
                            >
                                <Image
                                    className="rounded-md"
                                    src={`${env.NEXT_PUBLIC_API_URL}/player/${player}/head.png`}
                                    alt={`${player}'s Head`}
                                    width={64}
                                    height={64}
                                    draggable={false}
                                    unoptimized
                                />
                                <Separator className="mt-2" />
                                <div className="p-1.5 w-full text-center font-semibold bg-muted/50">
                                    {player}
                                </div>
                            </SimpleLink>
                        </div>
                    </SimpleTooltip>
                ))}
            </CardContent>
            <CardFooter>
                Here are some example players you can quickly view the data for.
            </CardFooter>
        </Card>
    );
};
export default PlayerExamples;
