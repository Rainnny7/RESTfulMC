import { getCachedMinecraftServer } from "@/lib/cached-api";
import { notFound } from "next/navigation";
import {
    JavaMinecraftServer,
    MinecraftServer,
    ServerPlatform,
} from "restfulmc-lib";

const ServerPage = async ({
    params,
}: PageProps<"/server/[platform]/[query]">) => {
    const { platform, query } = await params;
    let server: MinecraftServer;
    try {
        server = await getCachedMinecraftServer(
            platform as ServerPlatform,
            query
        );
    } catch {
        notFound();
    }
    return (
        <main className="min-h-screen flex flex-col">
            {JSON.stringify(server)}
        </main>
    );
};

export const generateMetadata = async ({
    params,
}: PageProps<"/server/[platform]/[query]">) => {
    try {
        const { platform, query } = await params;
        const server: MinecraftServer = await getCachedMinecraftServer(
            platform as ServerPlatform,
            query
        );
        const iconUrl: string | undefined =
            platform === "java"
                ? (server as JavaMinecraftServer).favicon?.url
                : undefined;
        return {
            title: server.hostname,
            description: `View the server ${server.hostname} on RESTfulMC.`,
            icons: {
                icon: iconUrl,
            },
            openGraph: {
                images: [
                    {
                        url: iconUrl,
                    },
                ],
            },
        };
    } catch {
        notFound();
    }
};

export default ServerPage;
