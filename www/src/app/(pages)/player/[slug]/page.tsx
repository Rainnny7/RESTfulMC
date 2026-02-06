import PlayerCape from "@/components/player/player-cape";
import PlayerDetails from "@/components/player/player-details";
import PlayerHeadCommands from "@/components/player/player-head-commands";
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
        <main className="min-h-screen flex flex-col">
            <PlayerHeader player={player} />
            <div className="-mt-16 px-5 flex flex-col-reverse lg:flex-row justify-center gap-5 items-center lg:items-start">
                <div className="w-full sm:w-auto flex flex-col sm:flex-row lg:flex-col gap-5">
                    <PlayerSkin player={player} />
                    <PlayerCape player={player} />
                </div>
                <div className="flex flex-col gap-5">
                    <PlayerDetails player={player} />
                    <PlayerHeadCommands player={player} />
                </div>
            </div>
        </main>
    );
};

export const generateMetadata = async ({
    params,
}: PageProps<"/player/[slug]">) => {
    try {
        const player: CachedPlayer = await getPlayer((await params).slug);
        const skullUrl: string = player.skin.parts.HEAD;
        return {
            title: `${player.username}'s Profile`,
            description: `View the profile of ${player.username} on RESTfulMC.`,
            icons: {
                icon: skullUrl,
            },
            openGraph: {
                images: [
                    {
                        url: skullUrl,
                    },
                ],
            },
        };
    } catch {
        notFound();
    }
};

export default PlayerPage;
