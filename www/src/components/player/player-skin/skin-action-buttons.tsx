"use client";

import SimpleTooltip from "@/components/simple-tooltip";
import { Button } from "@/components/ui/button";
import { downloadFile } from "@/lib/download";
import { formatSkinPartName } from "@/lib/skin";
import { DownloadIcon } from "lucide-react";
import { ReactElement } from "react";
import { CachedPlayer, SkinPart } from "restfulmc-lib";
import { toast } from "sonner";

const SkinActionButtons = ({
    player,
    displayedPart,
}: {
    player: CachedPlayer;
    displayedPart: SkinPart;
}): ReactElement => {
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
            {/* Download Part */}
            <SimpleTooltip content="Download Skin" side="bottom">
                <Button
                    variant="outline"
                    size="icon"
                    onClick={handleDownloadSkinPart}
                >
                    <DownloadIcon className="size-4" />
                </Button>
            </SimpleTooltip>
        </div>
    );
};
export default SkinActionButtons;
