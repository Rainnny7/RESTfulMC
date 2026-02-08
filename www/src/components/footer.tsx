"use client";

import { config } from "@/app/config";
import AppLogo from "@/components/app-logo";
import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import { env } from "@/lib/env";
import { SocialLink } from "@/types/config";
import Image from "next/image";
import { memo, ReactElement, useEffect, useReducer } from "react";

type FooterLink = {
    label: string;
    href: string;
};

const footerLinks: Record<string, FooterLink[]> = {
    "Quick Links": [
        {
            label: "Player / Server Lookup",
            href: "/",
        },
        {
            label: "Capes",
            href: "/capes",
        },
        {
            label: "Status",
            href: "/status",
        },
    ],
    Resources: [
        {
            label: "Documentation",
            href: `${env.NEXT_PUBLIC_API_URL}/docs`,
        },
        {
            label: "Source Code",
            href: "https://github.com/Rainnny7/RESTfulMC",
        },
    ],
};

const Footer = (): ReactElement => (
    <div className="relative mt-14 border-t border-border [background:radial-gradient(125%_125%_at_50%_10%,var(--color-alternative-background)_45%,var(--color-footer-background)_100%)]">
        {/* Content */}
        <footer className="mx-auto px-5 py-10 w-full max-w-7xl flex flex-col gap-10 text-sm text-white/30 font-medium">
            {/* Top */}
            <div className="flex flex-col md:flex-row justify-between gap-7 items-center md:items-start">
                {/* Branding */}
                <div className="flex gap-3 items-center">
                    <AppLogo size={40} />

                    {/* Hehe */}
                    <div className="flex flex-col gap-1 font-mono">
                        <AnimatedLine
                            initialDashes={27}
                            reverseDirection={false}
                        />
                        <span>
                            | The{" "}
                            <span className="bg-linear-to-br from-primary to-zinc-300/75 bg-clip-text text-transparent">
                                ultimate
                            </span>{" "}
                            Minecraft API |
                        </span>
                        <AnimatedLine initialDashes={27} reverseDirection />
                    </div>
                </div>

                {/* Links */}
                <div className="flex flex-wrap gap-12 items-start">
                    {Object.entries(footerLinks).map(([category, links]) => (
                        <div key={category} className="flex flex-col gap-1">
                            <h3 className="text-base text-primary font-semibold">
                                {category}
                            </h3>
                            <div className="flex flex-col gap-1">
                                {links.map((link: FooterLink) => (
                                    <SimpleLink
                                        key={link.href}
                                        href={link.href}
                                    >
                                        {link.label}
                                    </SimpleLink>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {/* Bottom */}
            <div className="flex flex-col md:flex-row justify-between gap-7 items-center md:items-start">
                {/* Copyright */}
                <div className="flex flex-col">
                    <span>
                        &copy; {new Date().getFullYear()} RESTfulMC. All rights
                        reserved.
                    </span>
                    <span>Not affiliated with Microsoft or Mojang AB.</span>
                </div>

                {/* Socials */}
                <div className="flex gap-2.5 items-center">
                    {config.socials.map((social: SocialLink) => (
                        <SimpleTooltip
                            key={social.href}
                            content={social.tooltip}
                            side="top"
                        >
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
            </div>
        </footer>
    </div>
);

type AnimatedLineState = { dashes: number; direction: "left" | "right" };

const animatedLineReducer = (
    state: AnimatedLineState,
    initialDashes: number
): AnimatedLineState => {
    if (state.dashes <= 0) {
        return { dashes: 1, direction: "right" };
    }
    if (state.dashes >= initialDashes) {
        return { dashes: initialDashes - 1, direction: "left" };
    }
    return {
        dashes:
            state.direction === "left" ? state.dashes - 1 : state.dashes + 1,
        direction: state.direction,
    };
};

const AnimatedLine = memo(
    ({
        initialDashes = 12,
        reverseDirection = false,
    }: {
        initialDashes?: number;
        reverseDirection?: boolean;
    }) => {
        const [state, dispatch] = useReducer(animatedLineReducer, {
            dashes: reverseDirection ? 0 : initialDashes,
            direction: reverseDirection ? "right" : "left",
        });

        useEffect(() => {
            const interval = setInterval(() => {
                dispatch(initialDashes);
            }, 70);

            return () => clearInterval(interval);
        }, [initialDashes]);

        const generateLine = () => {
            const leftDashes = "-".repeat(state.dashes);
            const rightDashes = "-".repeat(initialDashes - state.dashes);
            return `|${leftDashes}${
                state.direction === "left" ? "<" : ">"
            }${rightDashes}|`;
        };

        return <span>{generateLine()}</span>;
    }
);

AnimatedLine.displayName = "AnimatedLine";

export default Footer;
