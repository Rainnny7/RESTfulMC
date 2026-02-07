"use client";

import SimpleTooltip from "@/components/simple-tooltip";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { MoonStarIcon, SunIcon } from "lucide-react";
import { motion } from "motion/react";
import { useTheme } from "next-themes";
import { useRef } from "react";
import { flushSync } from "react-dom";

type props = {
    className?: string;
};

const ThemeSwitcher = ({ className }: props) => {
    const { theme, setTheme } = useTheme();
    const isDark: boolean = theme === "dark";
    const buttonRef = useRef<HTMLButtonElement | null>(null);

    const changeTheme = async () => {
        if (!buttonRef.current) return;

        await document.startViewTransition(() => {
            flushSync(() => {
                const dark = document.documentElement.classList.toggle("dark");
                setTheme(dark ? "dark" : "light");
            });
        }).ready;

        const { top, left, width, height } =
            buttonRef.current.getBoundingClientRect();
        const y = top + height / 2;
        const x = left + width / 2;

        const right = window.innerWidth - left;
        const bottom = window.innerHeight - top;
        const maxRad = Math.hypot(Math.max(left, right), Math.max(top, bottom));

        document.documentElement.animate(
            {
                clipPath: [
                    `circle(0px at ${x}px ${y}px)`,
                    `circle(${maxRad}px at ${x}px ${y}px)`,
                ],
            },
            {
                duration: 700,
                easing: "ease-in-out",
                pseudoElement: "::view-transition-new(root)",
            }
        );
    };
    return (
        <SimpleTooltip
            content={isDark ? "Burn my eyes" : "Back to the dark side"}
            side="bottom"
        >
            <div className="flex justify-center items-center">
                <Button
                    ref={buttonRef}
                    className={cn("size-8.5 rounded-xl", className)}
                    variant="outline"
                    size="icon"
                    onClick={changeTheme}
                >
                    <motion.div
                        initial={{ rotate: 0, scale: 1 }}
                        animate={{
                            rotate: !isDark ? 0 : -90,
                            scale: !isDark ? 1 : 0,
                        }}
                        transition={{ duration: 0.5 }}
                        className="absolute"
                    >
                        <SunIcon className="size-4.5" />
                    </motion.div>
                    <motion.div
                        initial={{ rotate: 90, scale: 0 }}
                        animate={{
                            rotate: !isDark ? 90 : 0,
                            scale: !isDark ? 0 : 1,
                        }}
                        transition={{ duration: 0.5 }}
                        className="absolute"
                    >
                        <MoonStarIcon className="size-4.5" />
                    </motion.div>
                </Button>
            </div>
        </SimpleTooltip>
    );
};

export default ThemeSwitcher;
