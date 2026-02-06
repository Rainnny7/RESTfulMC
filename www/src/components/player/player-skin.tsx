"use client";

import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {
    Tooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger,
} from "@/components/ui/tooltip";
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
        <TooltipProvider delayDuration={200}>
            <Card className="w-60">
                <CardHeader>
                    <CardTitle className="text-xl text-center">
                        Skin Preview
                    </CardTitle>
                </CardHeader>
                <CardContent className="h-60 flex justify-center">
                    <Image
                        className="object-center"
                        src={player.skin.parts[displayedPart]}
                        alt={`${player.username}'s ${displayedPart} skin`}
                        width={148}
                        height={148}
                        draggable={false}
                        unoptimized
                    />
                </CardContent>
                <CardFooter className="flex flex-wrap justify-center gap-1.5">
                    {SKIN_PARTS.map((part: SkinPart) => {
                        const partName: string = part
                            .replace(/_/g, " ")
                            .toLowerCase()
                            .replace(/\b\w/g, (c) => c.toUpperCase());
                        return (
                            <Tooltip key={part}>
                                <TooltipTrigger asChild>
                                    <button
                                        type="button"
                                        className="relative rounded-md overflow-hidden border-2 transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2"
                                        style={{
                                            width: 36,
                                            height: 36,
                                            borderColor:
                                                displayedPart === part
                                                    ? "hsl(var(--primary))"
                                                    : "transparent",
                                        }}
                                        onMouseEnter={() =>
                                            setHoveredPart(part)
                                        }
                                        onMouseLeave={() =>
                                            setHoveredPart(undefined)
                                        }
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
                                </TooltipTrigger>
                                <TooltipContent side="top">
                                    <p className="font-medium">{partName}</p>
                                    <p className="text-xs text-muted-foreground">
                                        Hover to preview · Click to select
                                    </p>
                                </TooltipContent>
                            </Tooltip>
                        );
                    })}
                </CardFooter>
            </Card>
        </TooltipProvider>
    );
};
export default PlayerSkin;
