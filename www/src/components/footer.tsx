"use client";

import AppLogo from "@/components/app-logo";
import { memo, ReactElement, useEffect, useState } from "react";

const Footer = (): ReactElement => {
    return (
        <footer className="mt-14 text-sm text-white/30 font-medium bg-muted/20 border-t border-border">
            <div className="mx-auto px-4 py-5 w-full max-w-7xl flex justify-between items-center">
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
