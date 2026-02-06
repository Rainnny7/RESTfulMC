"use client";

import SimpleTooltip from "@/components/simple-tooltip";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";
import Image from "next/image";
import { ReactElement, useState } from "react";
import { CachedPlayer, SkinPart } from "restfulmc-lib";

const SKIN_PARTS: SkinPart[] = [
    SkinPart.FULLBODY_FRONT,
    SkinPart.FULLBODY_BACK,
    SkinPart.BODY,
    SkinPart.HEAD,
    SkinPart.FACE,
];

const PlayerSkin = ({ player }: { player: CachedPlayer }): ReactElement => {
    const [selectedPart, setSelectedPart] = useState<SkinPart>(
        SkinPart.FULLBODY_FRONT
    );
    const [hoveredPart, setHoveredPart] = useState<SkinPart | undefined>(
        undefined
    );
    const displayedPart: SkinPart = hoveredPart ?? selectedPart;

    return (
        <Card className="w-60">
            <CardHeader>
                <CardTitle>Skin Preview</CardTitle>
            </CardHeader>
            <CardContent className="h-60 flex justify-center">
                <Image
                    className="object-contain"
                    src={player.skin.parts[displayedPart]}
                    alt={`${player.username}'s ${displayedPart} Skin`}
                    width={148}
                    height={148}
                    draggable={false}
                    unoptimized
                />
            </CardContent>
            <CardFooter className="flex flex-wrap justify-center gap-1">
                {SKIN_PARTS.map((part: SkinPart) => {
                    const partName: string = part
                        .replace(/_/g, " ")
                        .toLowerCase()
                        .replace(/\b\w/g, (c) => c.toUpperCase());
                    return (
                        <SimpleTooltip
                            key={part}
                            content={
                                <>
                                    <p className="font-medium">{partName}</p>
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
                                    displayedPart === part && "border-primary"
                                )}
                                onMouseEnter={() => setHoveredPart(part)}
                                onMouseLeave={() => setHoveredPart(undefined)}
                                onClick={() => setSelectedPart(part)}
                            >
                                <Image
                                    className="object-cover"
                                    src={player.skin.parts[part]}
                                    alt={partName}
                                    width={36}
                                    height={36}
                                    draggable={false}
                                    unoptimized
                                />
                            </button>
                        </SimpleTooltip>
                    );
                })}
            </CardFooter>
        </Card>
    );
};
export default PlayerSkin;
