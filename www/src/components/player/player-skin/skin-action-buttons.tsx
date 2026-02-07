"use client";

import SimpleTooltip from "@/components/simple-tooltip";
import { Button } from "@/components/ui/button";
import { downloadFile } from "@/lib/download";
import { formatSkinPartName } from "@/lib/skin";
import { useSkinProvider3D } from "@/providers/skin-provider-3d-provider";
import { skin3DAnimations } from "@/types/skin";
import { DownloadIcon, LayersIcon } from "lucide-react";
import Image from "next/image";
import { ReactElement, ReactNode } from "react";
import { CachedPlayer, SkinPart } from "restfulmc-lib";
import { toast } from "sonner";

const SkinActionButtons = ({
    player,
    displayedPart,
}: {
    player: CachedPlayer;
    displayedPart: SkinPart;
}): ReactElement => {
    const {
        animation,
        showElytra,
        showLayers,
        playAnimation,
        toggleShowElytra,
        toggleShowLayers,
    } = useSkinProvider3D();

    const handleDownloadSkinPart = () => {
        const filename: string = `${player.username}_${displayedPart.toLowerCase()}.png`;
        downloadFile(player.skin.parts[displayedPart], filename);
        toast.success(
            `Downloaded ${player.username}'s ${formatSkinPartName(displayedPart)}:`,
            {
                description: <code>{filename}</code>,
            }
        );
    };
    return (
        <div className="absolute -top-1.5 right-1 flex flex-col gap-1">
            {/* 3D Skin Controls */}
            {displayedPart === SkinPart.FULLBODY_FRONT && (
                <>
                    <SkinActionButton
                        tooltip="Play Idle Animation"
                        icon="/media/steve.webp"
                        isSelected={
                            animation.name === skin3DAnimations.idle.name
                        }
                        onClick={() => playAnimation(skin3DAnimations.idle)}
                    />
                    <SkinActionButton
                        tooltip="Play Walk Animation"
                        icon="/media/steve-walking.webp"
                        isSelected={
                            animation.name === skin3DAnimations.walking.name
                        }
                        onClick={() => playAnimation(skin3DAnimations.walking)}
                    />
                    <SkinActionButton
                        tooltip="Play Sneaking Animation"
                        icon="/media/steve-sneaking.webp"
                        isSelected={
                            animation.name === skin3DAnimations.sneaking.name
                        }
                        onClick={() => playAnimation(skin3DAnimations.sneaking)}
                    />
                    <SkinActionButton
                        tooltip="Play Fly Animation"
                        icon="/media/feather.webp"
                        isSelected={
                            animation.name === skin3DAnimations.flying.name
                        }
                        onClick={() => playAnimation(skin3DAnimations.flying)}
                    />
                    {player.cape && (
                        <SkinActionButton
                            tooltip="Toggle Elytra"
                            icon="/media/elytra.webp"
                            isSelected={
                                showElytra ||
                                animation.name === skin3DAnimations.flying.name
                            }
                            onClick={toggleShowElytra}
                        />
                    )}
                    <SkinActionButton
                        tooltip="Toggle Layers"
                        icon={<LayersIcon />}
                        isSelected={showLayers}
                        onClick={toggleShowLayers}
                    />
                </>
            )}

            {/* Download Skin Part */}
            <SkinActionButton
                tooltip="Download Skin"
                icon={<DownloadIcon />}
                onClick={handleDownloadSkinPart}
            />
        </div>
    );
};

const SkinActionButton = ({
    tooltip,
    icon,
    isSelected,
    onClick,
}: {
    tooltip: string;
    icon: string | ReactNode;
    isSelected?: boolean;
    onClick: () => void;
}) => (
    <SimpleTooltip content={isSelected ? undefined : tooltip} side="right">
        <Button
            variant={isSelected ? "default" : "outline"}
            size="icon"
            onClick={() => {
                if (!isSelected) {
                    onClick();
                }
            }}
        >
            {typeof icon === "string" ? (
                <Image
                    className="object-contain"
                    src={icon}
                    alt={tooltip}
                    width={14}
                    height={14}
                    draggable={false}
                />
            ) : (
                icon
            )}
        </Button>
    </SimpleTooltip>
);

export default SkinActionButtons;
