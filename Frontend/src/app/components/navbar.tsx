/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
"use client";

import GitHubStarButton from "@/components/button/github-star-button";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import Image from "next/image";
import { usePathname } from "next/navigation";
import { ReactElement } from "react";
import SimpleLink from "@/components/simple-link";

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
                <SimpleLink
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
                </SimpleLink>

                {/* Center - Links */}
                <div className="ml-auto absolute right-3 xs:inset-x-0 md:left-28 lg:left-0 flex justify-center md:justify-start lg:justify-center">
                    <div className="flex gap-3.5 xs:gap-7">
                        {Object.entries(config.navbarLinks).map(
                            (link: [string, string], index: number) => {
                                const url: string = link[1]; // The href of the link
                                let active: boolean = path.startsWith(url); // Is this the active link?
                                return (
                                    <SimpleLink
                                        key={index}
                                        className={cn(
                                            "font-semibold uppercase hover:text-minecraft-green-4 transition-all transform-gpu",
                                            active && "text-minecraft-green-4"
                                        )}
                                        href={url}
                                    >
                                        {link[0]}
                                    </SimpleLink>
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
