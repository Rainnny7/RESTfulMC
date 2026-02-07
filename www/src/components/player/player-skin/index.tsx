"use client";

import SkinActionButtons from "@/components/player/player-skin/skin-action-buttons";
import SkinPartSelector from "@/components/player/player-skin/skin-part-selector";
import PlayerSkinViewer3D from "@/components/player/player-skin/skin-renderer-3d";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import Image from "next/image";
import { ReactElement, useState } from "react";
import { CachedPlayer, SkinPart } from "restfulmc-lib";

const PlayerSkin = ({ player }: { player: CachedPlayer }): ReactElement => {
    const [selectedPart, setSelectedPart] = useState<SkinPart>(
        SkinPart.FULLBODY_FRONT
    );
    const [hoveredPart, setHoveredPart] = useState<SkinPart | undefined>(
        undefined
    );
    const displayedPart: SkinPart = hoveredPart ?? selectedPart;

    return (
        <Card className="w-full sm:w-60">
            <CardHeader>
                <CardTitle>Skin Preview</CardTitle>
            </CardHeader>
            <CardContent className="relative h-60 flex justify-center">
                {/* Preview */}
                {displayedPart === SkinPart.FULLBODY_FRONT ? (
                    <PlayerSkinViewer3D player={player} />
                ) : (
                    <Image
                        className="object-contain"
                        src={player.skin.parts[displayedPart]}
                        alt={`${player.username}'s ${displayedPart} Skin`}
                        width={148}
                        height={148}
                        draggable={false}
                        unoptimized
                    />
                )}

                {/* Action Buttons */}
                <SkinActionButtons
                    player={player}
                    displayedPart={displayedPart}
                />
            </CardContent>
            <CardFooter
                className="flex flex-wrap justify-center gap-1"
                onMouseLeave={() => setHoveredPart(undefined)}
            >
                <SkinPartSelector
                    player={player}
                    displayedPart={displayedPart}
                    setSelectedPart={setSelectedPart}
                    setHoveredPart={setHoveredPart}
                />
            </CardFooter>
        </Card>
    );
};
export default PlayerSkin;
