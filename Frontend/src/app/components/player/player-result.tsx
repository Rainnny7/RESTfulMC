import CopyButton from "@/components/button/copy-button";
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
import CodeDialog from "@/components/code/code-dialog";
import RawJson from "@/components/badge/raw-json";
import SimpleTooltip from "@/components/simple-tooltip";

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
 * @param query the original query to lookup this player
 * @param player the player to display
 * @returns the player result jsx
 */
const PlayerResult = ({ query, player }: PlayerResultProps): ReactElement => (
    <ContextMenu>
        <ContextMenuTrigger>
            <div className="relative px-2 py-7 flex flex-col items-center bg-muted rounded-xl">
                {/* Raw Json */}
                <div className="absolute top-2 right-2">
                    <CodeDialog
                        title="Raw Player Data"
                        description={`The raw JSON data for the player ${player.username}:`}
                        language="json"
                        trigger={<RawJson />}
                    >
                        {JSON.stringify(player, undefined, 4)}
                    </CodeDialog>
                </div>

                {/* Result */}
                <div className="w-full flex flex-col gap-3 justify-center items-center divide-y divide-zinc-700">
                    {/* Details */}
                    <div className="flex gap-5 items-center">
                        {/* Player Head */}
                        <Image
                            className="w-24 h-24 sm:w-28 sm:h-28 md:w-32 md:h-32 pointer-events-none"
                            src={player.skin.parts.HEAD}
                            alt={`${player.username}'s Head`}
                            width={128}
                            height={128}
                        />

                        {/* Name, Unique ID, and Badges */}
                        <div className="flex flex-col gap-1.5">
                            <h1 className="text-xl font-bold text-minecraft-green-3">
                                {player.username}
                            </h1>
                            <code className="text-xs xs:text-sm text-zinc-300">
                                {player.uniqueId}
                            </code>

                            {/* Legacy Badge */}
                            {player.legacy && (
                                <p className="text-sm font-semibold uppercase">
                                    Legacy
                                </p>
                            )}
                        </div>
                    </div>

                    {/* Skin Parts */}
                    <div className="pt-3 w-[90%] flex flex-col gap-3">
                        {/* Header */}
                        <h1 className="font-semibold uppercase select-none pointer-events-none">
                            Skin Parts
                        </h1>

                        {/* Skin Parts */}
                        <div className="flex gap-5">
                            {Object.entries(player.skin.parts)
                                .filter(
                                    ([part]) =>
                                        part === SkinPart.HEAD ||
                                        part === SkinPart.FACE ||
                                        part === SkinPart.BODY_FLAT
                                )
                                .map(
                                    (
                                        [part, url]: [string, string],
                                        index: number
                                    ) => (
                                        <SimpleTooltip
                                            key={index}
                                            content={`Click to view ${player.username}'s ${part}`}
                                        >
                                            <Link href={url} target="_blank">
                                                <div className="relative w-20 h-20">
                                                    <Image
                                                        className="object-contain hover:scale-[1.02] transition-all transform-gpu"
                                                        src={url}
                                                        alt={`${player.username}'s ${part}`}
                                                        fill
                                                    />
                                                </div>
                                            </Link>
                                        </SimpleTooltip>
                                    )
                                )}
                        </div>
                    </div>
                </div>
            </div>
        </ContextMenuTrigger>
        <ContextMenuContent className="flex flex-col">
            <CopyButton content={player.username} showToast>
                <ContextMenuItem>Copy Player Username</ContextMenuItem>
            </CopyButton>

            <CopyButton content={player.uniqueId} showToast>
                <ContextMenuItem>Copy Player UUID</ContextMenuItem>
            </CopyButton>

            <CopyButton content={`${config.siteUrl}/player/${query}`} showToast>
                <ContextMenuItem>Copy Share URL</ContextMenuItem>
            </CopyButton>
        </ContextMenuContent>
    </ContextMenu>
);
export default PlayerResult;
