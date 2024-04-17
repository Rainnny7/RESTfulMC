"use client";

import GitHubStarButton from "@/components/github-star-button";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { ReactElement } from "react";

/**
 * The navbar for the site.
 *
 * @returns the navbar jsx
 */
const Navbar = (): ReactElement => {
    const path: string = usePathname(); // Get the current path
    return (
        <nav className="fixed inset-x-0 flex h-16 sm:px-12 justify-center sm:justify-between items-center bg-navbar-background z-50">
            {/* Left */}
            <div className="flex gap-3 xs:gap-7 lg:gap-12 items-center transition-all transform-gpu">
                {/* App Branding */}
                <Link
                    className={cn(
                        "text-3xl text-minecraft-green-3 hover:opacity-85 transition-all transform-gpu",
                        minecrafter.className
                    )}
                    href="/"
                >
                    {/* Small Screens */}
                    <Image
                        className="lg:hidden"
                        src="/media/logo.webp"
                        alt="Site Logo"
                        width={42}
                        height={42}
                    />

                    {/* Large Screens */}
                    <span className="hidden lg:flex">{config.siteName}</span>
                </Link>

                {/* Links */}
                <div className="flex gap-7">
                    {Object.entries(config.navbarLinks).map(
                        (link: [string, string], index: number) => {
                            const url: string = link[1]; // The href of the link
                            let active: boolean = path.startsWith(url); // Is this the active link?
                            return (
                                <Link
                                    key={index}
                                    className={cn(
                                        "font-semibold uppercase hover:text-minecraft-green-4 transition-all transform-gpu",
                                        active && "text-minecraft-green-4"
                                    )}
                                    href={url}
                                >
                                    {link[0]}
                                </Link>
                            );
                        }
                    )}
                </div>
            </div>

            {/* Social Buttons - Right */}
            <div className="hidden md:flex">
                {/* Star on Github <3 */}
                <GitHubStarButton />
            </div>
        </nav>
    );
};
export default Navbar;
