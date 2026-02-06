"use client";

import { config } from "@/app/config";
import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import { SocialLink } from "@/types/config";
import Image from "next/image";
import { ReactElement } from "react";

const Socials = (): ReactElement => (
    <div className="flex gap-2.5 items-center">
        {config.socials.map((social: SocialLink) => (
            <SimpleTooltip key={social.href} content={social.tooltip}>
                <div className="relative size-[22px]">
                    <SimpleLink
                        href={social.href}
                        className="relative block size-full"
                    >
                        <Image
                            src={social.logo}
                            alt={social.href}
                            sizes="22px"
                            fill
                            draggable={false}
                            style={{ objectFit: "contain" }}
                        />
                    </SimpleLink>
                </div>
            </SimpleTooltip>
        ))}
    </div>
);
export default Socials;