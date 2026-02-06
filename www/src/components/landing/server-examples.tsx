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

const EXAMPLE_SERVERS: string[] = [
    "hypixel.net",
    "cubecraft.net",
    "wildnetwork.net",
    "mineplex.com",
    "aetheria.cc",
];

const PlayerExamples = (): ReactElement => (
    <Card className="w-full max-w-xl bg-card/45 backdrop-blur-md">
        <CardHeader>
            <CardTitle>Server Examples</CardTitle>
        </CardHeader>
        <CardContent className="flex flex-wrap justify-center gap-1 items-center">
            {EXAMPLE_SERVERS.map((server: string) => (
                <SimpleTooltip
                    key={server}
                    content={`Click to view ${server}'s Data`}
                    side="bottom"
                >
                    <div className="p-1 bg-muted/90 border border-border rounded-lg">
                        <SimpleLink href={`/server/${server}`}>
                            <Image
                                className="rounded-md"
                                src={`${env.NEXT_PUBLIC_API_URL}/server/${server}/icon.png`}
                                alt={`${server}'s Icon`}
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
            Here are some example servers you can quickly view the data for.
        </CardFooter>
    </Card>
);
export default PlayerExamples;
