import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { env } from "@/lib/env";
import Image from "next/image";
import { ReactElement } from "react";

const EXAMPLE_PLAYERS: string[] = [
    "Steve",
    "Alex",
    "Dinnerbone",
    "Grumm",
    "Rainnny",
];

const PlayerExamples = (): ReactElement => (
    <Card className="w-full max-w-xl bg-card/45 backdrop-blur-md">
        <CardHeader>
            <CardTitle>Player Examples</CardTitle>
        </CardHeader>
        <CardContent className="flex flex-wrap justify-center gap-1 items-center">
            {EXAMPLE_PLAYERS.map((player: string) => (
                <SimpleTooltip
                    key={player}
                    content={`Click to view ${player}'s Profile`}
                    side="bottom"
                >
                    <div className="p-1 bg-muted/90 border border-border rounded-lg">
                        <SimpleLink href={`/player/${player}`}>
                            <Image
                                className="rounded-md"
                                src={`${env.NEXT_PUBLIC_API_URL}/player/${player}/head.png`}
                                alt={`${player}'s Head`}
                                width={56}
                                height={56}
                                draggable={false}
                                unoptimized
                            />
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
export default PlayerExamples;
