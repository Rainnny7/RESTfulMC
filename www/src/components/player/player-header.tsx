import PageHeader from "@/components/page-header";
import PlayerUUID from "@/components/player/player-uuid";
import Image from "next/image";
import { ReactElement } from "react";
import { CachedPlayer } from "restfulmc-lib";

const PlayerHeader = ({ player }: { player: CachedPlayer }): ReactElement => (
    <PageHeader
        className="min-h-112"
        contentClassName="gap-7"
        backgroundImage="/media/background/player.webp"
    >
        {/* Player Head & Username */}
        <div className="flex gap-5 items-center">
            <Image
                src={player.skin.parts.HEAD}
                alt={`${player.username}'s Head`}
                width={86}
                height={86}
                priority
                unoptimized
                draggable={false}
            />
            <h1 className="text-5xl font-black">{player.username}</h1>
        </div>

        {/* Player UUID */}
        <PlayerUUID player={player} />
    </PageHeader>
);
export default PlayerHeader;
