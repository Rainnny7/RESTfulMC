import PlayerHeader from "@/components/player/player-header";
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
