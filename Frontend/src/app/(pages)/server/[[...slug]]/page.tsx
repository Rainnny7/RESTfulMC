import Embed from "@/components/embed";
import ServerResult from "@/components/server/server-result";
import ServerSearch from "@/components/server/server-search";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { minecrafter } from "@/font/fonts";
import { capitalize } from "@/lib/stringUtils";
import { cn } from "@/lib/utils";
import { PageProps } from "@/types/page";
import { ExclamationCircleIcon } from "@heroicons/react/24/outline";
import { Metadata, Viewport } from "next";
import { ReactElement } from "react";
import {
    CachedBedrockMinecraftServer,
    CachedJavaMinecraftServer,
    getMinecraftServer,
    type RestfulMCAPIError,
    ServerPlatform,
} from "restfulmc-lib";

/**
 * The page to lookup a server.
 *
 * @returns the page jsx
 */
const ServerPage = async ({ params }: PageProps): Promise<ReactElement> => {
    let error: string | undefined = undefined; // The error to display
    let result:
        | CachedJavaMinecraftServer
        | CachedBedrockMinecraftServer
        | undefined = undefined; // The server to display
    const platform: string | undefined = params.slug?.[0]; // The platform to search for
    const hostname: string | undefined = params.slug?.[1]; // The hostname of the server to search for

    // Try and get the server to display
    try {
        result =
            platform && hostname
                ? await getMinecraftServer(platform as ServerPlatform, hostname)
                : undefined;
    } catch (err) {
        error = (err as RestfulMCAPIError).message; // Set the error message
    }

    // Render the page
    return (
        <main className="px-3 h-screen flex justify-center items-center">
            <div className="flex flex-col gap-7">
                <h1
                    className={cn(
                        "mt-20 text-6xl text-minecraft-green-3 text-center pointer-events-none",
                        minecrafter.className
                    )}
                >
                    Server Lookup
                </h1>

                <div className="flex flex-col gap-5 px-10 xs:px-16 transition-all transform-gpu">
                    {/* Error */}
                    {error && (
                        <Alert variant="destructive">
                            <ExclamationCircleIcon width={20} height={20} />
                            <AlertTitle>Error</AlertTitle>
                            <AlertDescription>{error}</AlertDescription>
                        </Alert>
                    )}

                    {/* Search */}
                    <ServerSearch platform={platform} hostname={hostname} />
                </div>

                {/* Server Result */}
                {result && (
                    <div className="flex justify-center scale-[.71] xs:scale-75 sm:scale-100 transition-all transform-gpu">
                        <ServerResult server={result} />
                    </div>
                )}
            </div>
        </main>
    );
};

/**
 * Generate metadata for this page.
 *
 * @param params the route params
 * @param searchParams the search params
 * @returns the generated metadata
 */
export const generateMetadata = async ({
    params,
}: PageProps): Promise<Metadata> => {
    const platform: string | undefined = params.slug?.[0]; // The platform to search for
    const hostname: string | undefined = params.slug?.[1]; // The hostname of the server to search for
    let embed: Metadata | undefined; // The server embed, if any
    if (platform && hostname) {
        try {
            const server:
                | CachedJavaMinecraftServer
                | CachedBedrockMinecraftServer = await getMinecraftServer(
                platform as ServerPlatform,
                hostname
            ); // Get the server to embed
            return Embed({
                title: `${capitalize(platform)} Server: ${server.hostname}`,
                description: `There are ${server.players.online.toLocaleString()}/${server.players.max.toLocaleString()} playing here!\n\nClick to view data about this server.`,
                thumbnail: (server as CachedJavaMinecraftServer).favicon?.url,
            });
        } catch (err) {
            const code: number = (err as RestfulMCAPIError).code; // Get the error status code
            if (code === 400) {
                return Embed({
                    title: "Invalid Platform or Hostname",
                    description: `The platform ${platform} or hostname ${hostname} is invalid.`,
                });
            } else if (code === 404) {
                return Embed({
                    title: "Server Not Found",
                    description: `The server ${hostname} was not found.`,
                });
            }
        }
    }

    // Return the page embed
    return embed
        ? embed
        : Embed({
              title: "Server Lookup",
              description: "Search for a server to view its data.",
          });
};

/**
 * Generate the viewport for this page.
 *
 * @param params the route params
 * @returns the generated metadata
 */
export const generateViewport = async ({
    params,
}: PageProps): Promise<Viewport> => {
    const platform: string | undefined = params.slug?.[0]; // The platform to search for
    const hostname: string | undefined = params.slug?.[1]; // The hostname of the server to search for
    if (platform && hostname) {
        try {
            await getMinecraftServer(platform as ServerPlatform, hostname); // Get the server to embed
            return { themeColor: "#55FF55" }; // Online
        } catch (err) {
            return { themeColor: "#AA0000" }; // Error
        }
    }
    return {};
};

export default ServerPage;
