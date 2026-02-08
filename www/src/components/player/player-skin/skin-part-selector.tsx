import SimpleTooltip from "@/components/simple-tooltip";
import { formatSkinPartName } from "@/lib/skin";
import { cn } from "@/lib/utils";
import Image from "next/image";
import { ReactElement } from "react";
import { CachedPlayer, SkinPart } from "restfulmc-lib";

const SKIN_PARTS: SkinPart[] = [
    SkinPart.FULLBODY_FRONT,
    SkinPart.FULLBODY_BACK,
    SkinPart.BODY,
    SkinPart.HEAD,
    SkinPart.FACE,
];

const SkinPartSelector = ({
    player,
    displayedPart,
    setSelectedPart,
    setHoveredPart,
}: {
    player: CachedPlayer;
    displayedPart: SkinPart;
    setSelectedPart: (part: SkinPart) => void;
    setHoveredPart: (part: SkinPart | undefined) => void;
}): ReactElement => (
    <>
        {SKIN_PARTS.map((part: SkinPart) => {
            const partName: string = formatSkinPartName(part);
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
                            "relative size-9 rounded-md border-2 overflow-hidden",
                            displayedPart === part && "border-primary"
                        )}
                        onMouseEnter={() => setHoveredPart(part)}
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
    </>
);
export default SkinPartSelector;
