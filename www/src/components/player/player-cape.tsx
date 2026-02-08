"use client";

import SimpleTooltip from "@/components/simple-tooltip";
import { Button } from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { downloadFile } from "@/lib/download";
import request from "@/lib/request";
import { cn } from "@/lib/utils";
import { DownloadIcon } from "lucide-react";
import Image from "next/image";
import { ReactElement, useEffect, useState } from "react";
import { CachedPlayer } from "restfulmc-lib";
import { toast } from "sonner";

type CapeType = {
    name: string;
    getUrl: (player: CachedPlayer) => Promise<string | undefined>;
};

const CAPE_TYPES: CapeType[] = [
    {
        name: "Minecraft",
        getUrl: async (player: CachedPlayer) => player.cape?.url ?? undefined,
    },
    {
        name: "Optifine",
        getUrl: async (player: CachedPlayer) => {
            const url: string = `https://cors.rainnny.club/http://s.optifine.net/capes/${player.username}.png`;
            try {
                await request.get(url);
                return url;
            } catch {
                return undefined;
            }
        },
    },
];

const PlayerCape = ({ player }: { player: CachedPlayer }): ReactElement => {
    const [capeUrls, setCapeUrls] = useState<Map<string, string>>(new Map());
    const [selectedType, setSelectedType] = useState<CapeType>(CAPE_TYPES[0]);
    const [hoveredType, setHoveredType] = useState<CapeType | undefined>(
        undefined
    );

    useEffect(() => {
        void Promise.all(
            CAPE_TYPES.map(async (type: CapeType) => ({
                type,
                url: await type.getUrl(player),
            }))
        ).then((results) => {
            const urls: Map<string, string> = new Map(
                results
                    .filter(
                        (result): result is { type: CapeType; url: string } =>
                            Boolean(result.url)
                    )
                    .map((result) => [result.type.name, result.url])
            );
            setCapeUrls(urls);
            setSelectedType(
                (prev) =>
                    results.find(
                        (result) => result.url && result.type.name === prev.name
                    )?.type ??
                    results.find((result) => result.url)?.type ??
                    prev
            );
        });
    }, [player]);

    const displayedType: CapeType = hoveredType ?? selectedType;
    const capeUrl: string | undefined = capeUrls.get(displayedType.name);

    const handleDownloadCape = () => {
        if (!capeUrl) return;
        const filename: string = `${player.username}_${displayedType.name.toLowerCase()}.png`;
        downloadFile(capeUrl, filename);
        toast.success(
            `Downloaded ${player.username}'s ${displayedType.name} Cape:`,
            {
                description: <code>{filename}</code>,
            }
        );
    };

    return (
        <Card className="w-full sm:w-76 h-fit">
            <CardHeader>
                <CardTitle>Cape Preview</CardTitle>
            </CardHeader>
            <CardContent className="relative h-40 flex justify-center items-center">
                {capeUrl ? (
                    <>
                        <Image
                            className="object-contain"
                            src={capeUrl}
                            alt={`${player.username}'s ${displayedType.name} Cape`}
                            width={148}
                            height={148}
                            draggable={false}
                            unoptimized
                        />

                        {/* Download Button */}
                        <SimpleTooltip content="Download Cape" side="bottom">
                            <Button
                                className="absolute -top-3 right-1"
                                variant="outline"
                                size="icon"
                                onClick={handleDownloadCape}
                            >
                                <DownloadIcon className="size-4" />
                            </Button>
                        </SimpleTooltip>
                    </>
                ) : (
                    <p className="text-sm text-muted-foreground">
                        No capes available
                    </p>
                )}
            </CardContent>
            {capeUrl && (
                <CardFooter
                    className="flex flex-wrap justify-center gap-1"
                    onMouseLeave={() => setHoveredType(undefined)}
                >
                    {CAPE_TYPES.filter((type: CapeType) =>
                        capeUrls.has(type.name)
                    ).map((type: CapeType) => {
                        const url: string | undefined = capeUrls.get(type.name);
                        return (
                            <SimpleTooltip
                                key={type.name}
                                content={
                                    <>
                                        <p className="font-medium">
                                            {type.name}
                                        </p>
                                        <p className="text-xs text-muted-foreground">
                                            Hover to preview · Click to select
                                        </p>
                                    </>
                                }
                                side="top"
                            >
                                <button
                                    type="button"
                                    className={cn(
                                        "relative size-8 rounded-md border-2 overflow-hidden",
                                        displayedType.name === type.name &&
                                            "border-primary"
                                    )}
                                    onMouseEnter={() => setHoveredType(type)}
                                    onClick={() => setSelectedType(type)}
                                >
                                    {url ? (
                                        <Image
                                            className="object-cover"
                                            src={url}
                                            alt={`${player.username}'s ${type.name} Cape`}
                                            width={36}
                                            height={36}
                                            draggable={false}
                                            unoptimized
                                        />
                                    ) : (
                                        <div className="flex size-full items-center justify-center bg-muted text-xs">
                                            {type.name[0]}
                                        </div>
                                    )}
                                </button>
                            </SimpleTooltip>
                        );
                    })}
                </CardFooter>
            )}
        </Card>
    );
};
export default PlayerCape;
