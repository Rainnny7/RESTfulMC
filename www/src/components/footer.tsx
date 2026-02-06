"use client";

import AppLogo from "@/components/app-logo";
import SimpleLink from "@/components/simple-link";
import { env } from "@/lib/env";
import { memo, ReactElement, useEffect, useState } from "react";

type FooterLink = {
    label: string;
    href: string;
};

const footerLinks: Record<string, FooterLink[]> = {
    Resources: [
        {
            label: "Documentation",
            href: `${env.NEXT_PUBLIC_BASE_URL}/api/docs`,
        },
        {
            label: "Source Code",
            href: "https://github.com/Rainnny7/RESTfulMC",
        },
    ],
};

const Footer = (): ReactElement => {
    return (
        <footer className="mt-14 mx-auto px-5 py-10 w-full max-w-7xl flex flex-col gap-10 text-sm text-white/30 font-medium border-t border-border">
            {/* Top */}
            <div className="flex justify-between items-start">
                {/* Branding */}
                <div className="flex gap-3 items-center">
                    <AppLogo size={40} />

                    {/* Hehe */}
                    <div className="flex flex-col gap-1">
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
                <div className="flex gap-5 items-center">
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

            {/* Bottom - Copyright */}
            <div className="flex flex-col">
                <span>
                    &copy; {new Date().getFullYear()} RESTfulMC. All rights
                    reserved.
                </span>
                <span>Not affiliated with Microsoft or Mojang AB.</span>
            </div>
        </footer>
    );
};

const AnimatedLine = memo(
    ({
        initialDashes = 12,
        reverseDirection = false,
    }: {
        initialDashes?: number;
        reverseDirection?: boolean;
    }) => {
        const [dashes, setDashes] = useState(
            reverseDirection ? 0 : initialDashes
        );
        const [direction, setDirection] = useState<"left" | "right">(
            reverseDirection ? "right" : "left"
        );

        useEffect(() => {
            const interval = setInterval(() => {
                setDashes((prev) => {
                    if (prev <= 0) {
                        setDirection("right");
                        return 1;
                    }
                    if (prev >= initialDashes) {
                        setDirection("left");
                        return initialDashes - 1;
                    }
                    return direction === "left" ? prev - 1 : prev + 1;
                });
            }, 70);

            return () => clearInterval(interval);
        }, [direction, initialDashes]);

        const generateLine = () => {
            const leftDashes = "-".repeat(dashes);
            const rightDashes = "-".repeat(initialDashes - dashes);
            return `|${leftDashes}${
                direction === "left" ? "<" : ">"
            }${rightDashes}|`;
        };

        return <span>{generateLine()}</span>;
    }
);

AnimatedLine.displayName = "AnimatedLine";

export default Footer;
