"use client";

import GitHubStarButton from "@/components/button/github-star-button";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
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
        <nav className="fixed inset-x-0 flex h-16 px-3 xs:px-5 sm:px-12 bg-navbar-background transition-all transform-gpu z-50">
            <div className="w-full flex justify-between items-center">
                {/* App Branding - Left */}
                <Link
                    className={cn(
                        "text-3xl text-minecraft-green-3 hover:opacity-85 transition-all transform-gpu z-50",
                        minecrafter.className
                    )}
                    href="/"
                >
                    {/* Small Screens */}
                    <Image
                        className="lg:hidden"
                        src="/media/logo.png"
                        alt="Site Logo"
                        width={38}
                        height={38}
                    />

                    {/* Large Screens */}
                    <span className="hidden lg:flex">{config.siteName}</span>
                </Link>

                {/* Center - Links */}
                <div className="ml-auto absolute right-3 xs:inset-x-0 md:left-28 lg:left-0 flex justify-center md:justify-start lg:justify-center">
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
            </div>
        </nav>
    );
};
export default Navbar;
