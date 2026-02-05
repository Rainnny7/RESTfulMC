import { getPlayer } from "restfulmc-lib";

const PlayerPage = async ({ params }: PageProps<"/player/[slug]">) => {
    const { slug } = await params;
    const player = await getPlayer(slug);
    return <main className="min-h-screen pt-24">{JSON.stringify(player)}</main>;
};
export default PlayerPage;
