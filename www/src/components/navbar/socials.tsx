"use client";

import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import Image from "next/image";
import { ReactElement } from "react";

type SocialLink = {
    logo: string;
    tooltip: string;
    href: string;
};

const socials: SocialLink[] = [
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
];

const Socials = (): ReactElement => (
    <div className="flex gap-2.5 items-center">
        {socials.map((social: SocialLink) => (
            <SimpleTooltip key={social.href} content={social.tooltip}>
                <div>
                    <SimpleLink href={social.href}>
                        <Image
                            src={social.logo}
                            alt={social.href}
                            width={22}
                            height={22}
                            draggable={false}
                        />
                    </SimpleLink>
                </div>
            </SimpleTooltip>
        ))}
    </div>
);
export default Socials;
