import { notFound } from "next/navigation";
import { CachedPlayer, getPlayer } from "restfulmc-lib";

const PlayerPage = async ({ params }: PageProps<"/player/[slug]">) => {
    let player: CachedPlayer;
    try {
        player = await getPlayer((await params).slug);
    } catch {
        notFound();
    }
    return <main className="min-h-screen pt-24">{JSON.stringify(player)}</main>;
};
export default PlayerPage;
