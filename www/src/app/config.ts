import { env } from "@/lib/env";
import { Config } from "@/types/config";

export const config: Config = {
    sourceCodeUrl: "https://github.com/Rainnny7/RESTfulMC",
    documentationUrl: `${env.NEXT_PUBLIC_API_URL}/swagger-ui/index.html`,
    socials: [
        {
            logo: "/media/logo/discord.svg",
            tooltip: "Click to join our Discord",
            href: "https://discord.rainnny.club",
        },
        {
            logo: "/media/logo/github.svg",
            tooltip: "Click to view the source code",
            href: "https://github.com/Rainnny7/RESTfulMC",
        },
    ],
};
