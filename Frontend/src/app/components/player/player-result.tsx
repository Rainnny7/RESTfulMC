import CopyButton from "@/components/copy-button";
import { Badge } from "@/components/ui/badge";
import {
    ContextMenu,
    ContextMenuContent,
    ContextMenuItem,
    ContextMenuTrigger,
} from "@/components/ui/context-menu";
import config from "@/config";
import Image from "next/image";
import Link from "next/link";
import { ReactElement } from "react";
import { CachedPlayer, SkinPart } from "restfulmc-lib";

/**
 * The props for a player result.
 */
type PlayerResultProps = {
    /**
     * The original query to lookup this player.
     */
    query: string | undefined;

    /**
     * The result of a search.
     */
    player: CachedPlayer;
};

/**
 * The result of a player search.
 *
 * @param player the player to display
 * @returns the player result jsx
 */
const PlayerResult = ({
    query,
    player: {
        uniqueId,
        username,
        skin: { parts },
        legacy,
    },
}: PlayerResultProps): ReactElement => (
    <ContextMenu>
        <ContextMenuTrigger>
            <div className="relative px-2 py-7 flex flex-col items-center bg-muted rounded-xl">
                {/* Raw Json */}
                <div className="absolute top-2 right-2">
                    <Link
                        href={`${config.apiEndpoint}/player/${username}`}
                        target="_blank"
                    >
                        <Badge className="bg-minecraft-green-2 hover:bg-minecraft-green-2 hover:opacity-85 text-white font-semibold uppercase transition-all transform-gpu">
                            Raw Json
                        </Badge>
                    </Link>
                </div>

                <div className="w-full flex flex-col gap-3 justify-center items-center divide-y divide-zinc-700">
                    {/* Details */}
                    <div className="flex gap-5 items-center">
                        {/* Player Head */}
                        <Image
                            className="w-24 h-24 sm:w-28 sm:h-28 md:w-32 md:h-32"
                            src={parts.HEAD}
                            alt={`${username}'s Head`}
                            width={128}
                            height={128}
                        />

                        {/* Name, Unique ID, and Badges */}
                        <div className="flex flex-col gap-1.5">
                            <h1 className="text-xl font-bold text-minecraft-green-3">
                                {username}
                            </h1>
                            <code className="text-xs xs:text-sm text-zinc-300">
                                {uniqueId}
                            </code>

                            {/* Legacy Badge */}
                            {legacy && (
                                <p className="text-sm font-semibold uppercase">
                                    Legacy
                                </p>
                            )}
                        </div>
                    </div>

                    {/* Skin Parts */}
                    <div className="pt-3 w-[90%] flex flex-col gap-3">
                        {/* Header */}
                        <h1 className="font-semibold uppercase">Skin Parts</h1>

                        {/* Skin Parts */}
                        <div className="flex gap-5">
                            {Object.entries(parts)
                                .filter(
                                    ([part]) =>
                                        part === SkinPart.HEAD ||
                                        part === SkinPart.FACE ||
                                        part === SkinPart.BODY_FLAT
                                )
                                .map(([part, url], index) => (
                                    <Link
                                        key={index}
                                        href={url}
                                        target="_blank"
                                    >
                                        <img
                                            className="h-20 sm:h-24 md:h-28 hover:scale-[1.02] transition-all transform-gpu"
                                            src={url}
                                            alt={`${username}'s ${part}`}
                                        />
                                    </Link>
                                ))}
                        </div>
                    </div>
                </div>
            </div>
        </ContextMenuTrigger>
        <ContextMenuContent className="flex flex-col">
            <CopyButton content={username} showToast>
                <ContextMenuItem>Copy Player Username</ContextMenuItem>
            </CopyButton>

            <CopyButton content={uniqueId} showToast>
                <ContextMenuItem>Copy Player UUID</ContextMenuItem>
            </CopyButton>

            <CopyButton content={`${config.siteUrl}/player/${query}`} showToast>
                <ContextMenuItem>Copy Share URL</ContextMenuItem>
            </CopyButton>
        </ContextMenuContent>
    </ContextMenu>
);
export default PlayerResult;
