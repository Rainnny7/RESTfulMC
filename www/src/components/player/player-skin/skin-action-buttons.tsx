"use client";

import SimpleTooltip from "@/components/simple-tooltip";
import { Button } from "@/components/ui/button";
import { downloadFile } from "@/lib/download";
import { formatSkinPartName } from "@/lib/skin";
import { cn } from "@/lib/utils";
import { useSkinProvider3D } from "@/providers/skin-provider-3d-provider";
import { skin3DAnimations } from "@/types/skin";
import {
    BedSingleIcon,
    ChevronUpIcon,
    DownloadIcon,
    FeatherIcon,
    FootprintsIcon,
    RotateCwIcon,
} from "lucide-react";
import { ReactElement, ReactNode } from "react";
import { CachedPlayer, SkinPart } from "restfulmc-lib";
import { toast } from "sonner";

const SkinActionButtons = ({
    player,
    selectedPart,
    displayedPart,
}: {
    player: CachedPlayer;
    selectedPart: SkinPart;
    displayedPart: SkinPart;
}): ReactElement => {
    const show3DControls: boolean = selectedPart === SkinPart.FULLBODY_FRONT;
    const { animation, isAutoRotating, playAnimation, toggleAutoRotating } =
        useSkinProvider3D();
    const isSneaking: boolean =
        animation.name === skin3DAnimations.sneaking.name;

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
        <div className="absolute -top-3 right-1 flex flex-col gap-1">
            {/* 3D Skin Controls */}
            {show3DControls && (
                <>
                    <SkinActionButton
                        tooltip="Play Idle Animation"
                        icon={<BedSingleIcon />}
                        isSelected={
                            animation.name === skin3DAnimations.idle.name
                        }
                        onClick={() => playAnimation(skin3DAnimations.idle)}
                    />
                    <SkinActionButton
                        tooltip="Play Walk Animation"
                        icon={<FootprintsIcon />}
                        isSelected={
                            animation.name === skin3DAnimations.walking.name
                        }
                        onClick={() => playAnimation(skin3DAnimations.walking)}
                    />
                    <SkinActionButton
                        tooltip="Play Fly Animation"
                        icon={<FeatherIcon />}
                        isSelected={
                            animation.name === skin3DAnimations.flying.name
                        }
                        onClick={() => playAnimation(skin3DAnimations.flying)}
                    />
                    <SkinActionButton
                        tooltip="Toggle Sneak"
                        icon={
                            <ChevronUpIcon
                                className={cn(
                                    "transition-transform duration-200 ease-in-out transform-gpu",
                                    isSneaking && "rotate-180"
                                )}
                            />
                        }
                        isSelected={isSneaking}
                        onClick={() => playAnimation(skin3DAnimations.sneaking)}
                    />
                    <SkinActionButton
                        tooltip="Toggle Auto Rotate"
                        icon={<RotateCwIcon />}
                        isSelected={isAutoRotating}
                        onClick={toggleAutoRotating}
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
    icon: ReactNode;
    isSelected?: boolean;
    onClick: () => void;
}) => (
    <SimpleTooltip content={isSelected ? undefined : tooltip} side="right">
        <Button
            variant={isSelected ? "default" : "outline"}
            size="icon-sm"
            onClick={onClick}
        >
            {icon}
        </Button>
    </SimpleTooltip>
);

export default SkinActionButtons;
