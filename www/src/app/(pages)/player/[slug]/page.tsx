import PlayerCape from "@/components/player/player-cape";
import PlayerDetails from "@/components/player/player-details";
import PlayerHeader from "@/components/player/player-header";
import PlayerSkin from "@/components/player/player-skin";
import { notFound } from "next/navigation";
import { CachedPlayer, getPlayer } from "restfulmc-lib";

const PlayerPage = async ({ params }: PageProps<"/player/[slug]">) => {
    let player: CachedPlayer;
    try {
        player = await getPlayer((await params).slug);
    } catch {
        notFound();
    }
    return (
        <main className="pb-10 min-h-screen flex flex-col">
            <PlayerHeader player={player} />
            <div className="-mt-7 flex justify-center gap-5 items-start">
                <div className="flex flex-col gap-5">
                    <PlayerSkin player={player} />
                    <PlayerCape player={player} />
                </div>
                <PlayerDetails player={player} />
            </div>
        </main>
    );
};

export const generateMetadata = async ({
    params,
}: PageProps<"/player/[slug]">) => {
    try {
        const player: CachedPlayer = await getPlayer((await params).slug);
        return {
            title: `${player.username}'s Profile`,
            description: `View the profile of ${player.username} on RESTfulMC.`,
            icons: {
                icon: player.skin.parts.FACE,
            },
            openGraph: {
                images: [
                    {
                        url: player.skin.parts.FACE,
                    },
                ],
            },
        };
    } catch {
        notFound();
    }
};

export default PlayerPage;
