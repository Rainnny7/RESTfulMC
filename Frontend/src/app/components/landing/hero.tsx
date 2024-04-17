import GitHubStarButton from "@/components/github-star-button";
import MinecraftButton from "@/components/minecraft-button";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import Link from "next/link";
import { ReactElement } from "react";

/**
 * The hero content.
 *
 * @returns the hero jsx
 */
const Hero = (): ReactElement => (
    <div className="pt-56 pb-40 flex flex-col gap-8 justify-center items-center">
        <div className="flex flex-col gap-4 items-center text-center">
            {/* Title */}
            <h1
                className={cn(
                    "text-5xl sm:text-6xl text-minecraft-green-3",
                    minecrafter.className
                )}
            >
                {config.siteName}
            </h1>

            {/* Subtitle */}
            <h2 className="text-xl">{config.metadata.description}</h2>
        </div>

        {/* Links */}
        <div className="flex gap-5 xs:gap-10">
            <Link href="/docs">
                <MinecraftButton className="w-44 h-12">
                    Get Started
                </MinecraftButton>
            </Link>

            {/* Star on Github <3 */}
            <div className="md:hidden">
                <GitHubStarButton />
            </div>
        </div>
    </div>
);
export default Hero;
