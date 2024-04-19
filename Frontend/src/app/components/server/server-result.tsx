import { cn } from "@/lib/utils";
import Image from "next/image";
import { ReactElement, ReactNode } from "react";
import {
    CachedBedrockMinecraftServer,
    CachedJavaMinecraftServer,
} from "restfulmc-lib";
import config from "../../config";

/**
 * The props for a server result.
 */
type ServerResultProps = {
    /**
     * The result of a search.
     */
    server: CachedJavaMinecraftServer | CachedBedrockMinecraftServer;
};

/**
 * The result of a server search.
 *
 * @param server the server to display
 * @returns the server result jsx
 */
const ServerResult = ({ server }: ServerResultProps): ReactElement => {
    const favicon: string | undefined = (server as CachedJavaMinecraftServer)
        .favicon?.url; // The favicon of the server (TODO: bedrock)
    return (
        <div
            className={cn(
                "w-[29rem] relative p-2 flex gap-2 rounded-lg pointer-events-none",
                'bg-[url("/media/server-background.png")]'
            )}
        >
            {/* Favicon */}
            <Image
                src={favicon || `${config.apiEndpoint}/server/icon/fallback`}
                alt={`${server.hostname}'s Favicon`}
                width={64}
                height={64}
            />

            {/* Name & MOTD */}
            <div className="flex flex-col">
                <h1>{server.hostname}</h1>
                {server.motd.html.map(
                    (line: string, index: number): ReactElement => {
                        return (
                            <p
                                key={index}
                                className="pointer-events-auto"
                                dangerouslySetInnerHTML={{ __html: line }}
                            ></p>
                        );
                    }
                )}
            </div>

            {/* Ping */}
            <div className="absolute top-0.5 right-0.5 flex gap-1 items-center">
                <p>
                    {server.players.online}/{server.players.max}
                </p>
                <Image
                    src="/media/full-ping.png"
                    alt="Ping!"
                    width={28}
                    height={28}
                />
            </div>
        </div>
    );
};
export default ServerResult;
