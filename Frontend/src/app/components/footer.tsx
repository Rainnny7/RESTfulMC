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

import Image from "next/image";
import { ReactElement } from "react";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import { FooterLinks } from "@/types/config";
import SimpleLink from "@/components/simple-link";

/**
 * The footer for the site.
 *
 * @returns the footer jsx
 */
const Footer = (): ReactElement => (
    <footer
        className={cn(
            `before:absolute before:top-0 before:left-0 before:w-full before:h-full before:bg-[url("/media/dark-wool-background.png")] before:bg-center before:bg-repeat`, // Background
            "relative inset-x-0 bottom-0 h-72 flex justify-center items-center", // Styling
            `after:absolute after:top-0 after:left-0 after:-translate-y-20 after:w-full after:h-20 after:bg-[url("/media/dark-wool-transition.png")] after:bg-center after:bg-repeat` // Top Border
        )}
    >
        <div className="xl:px-40 pb-14 md:pb-0 flex flex-col gap-7 md:flex-row md:gap-0 justify-around items-center z-50 w-full h-full transition-all transform-gpu">
            {/* Branding */}
            <div className="flex flex-col gap-1 justify-center select-none pointer-events-none">
                {/* Logo & Site Name */}
                <div className="flex gap-7 items-center">
                    <Image
                        src="/media/logo.png"
                        alt="Site Logo"
                        width={56}
                        height={56}
                    />

                    <h1
                        className={cn(
                            "text-4xl sm:text-5xl md:text-4xl lg:text-5xl text-minecraft-green-3",
                            minecrafter.className
                        )}
                    >
                        {config.siteName}
                    </h1>
                </div>

                {/* Copyright */}
                <div className="flex justify-center text-center text-zinc-400/80">
                    <p className="max-w-xs sm:max-w-sm">
                        Made with <span className="animate-pulse">💚</span> by{" "}
                        <SimpleLink
                            className="text-minecraft-green-4 opacity-100 hover:opacity-85 pointer-events-auto transition-all transform-gpu"
                            href="https://github.com/Rainnny7"
                            noRef
                            newTab
                        >
                            Braydon
                        </SimpleLink>
                        . Copyright © {new Date().getFullYear()}, All Rights
                        Reserved.
                    </p>
                </div>
            </div>

            {/* Links */}
            <div className="flex gap-16">
                {Object.keys(config.footerLinks).map(
                    (header: string, groupIndex: number): ReactElement => (
                        <div key={groupIndex} className="flex flex-col gap-2">
                            {/* Header */}
                            <h1
                                className={cn(
                                    "text-2xl xs:text-3xl text-minecraft-green-3 select-none pointer-events-none",
                                    minecrafter.className
                                )}
                            >
                                {header}
                            </h1>

                            {/* Links */}
                            <div className="flex flex-col gap-1">
                                {Object.entries(
                                    (config.footerLinks as FooterLinks)[header]
                                ).map(
                                    (
                                        [name, url]: [string, string],
                                        linkIndex: number
                                    ): ReactElement => (
                                        <SimpleLink
                                            key={linkIndex}
                                            className="font-semibold hover:opacity-85 transition-all transform-gpu"
                                            href={url}
                                        >
                                            {name}
                                        </SimpleLink>
                                    )
                                )}
                            </div>
                        </div>
                    )
                )}
            </div>

            {/* Disclaimer */}
            <p className="absolute inset-x-0 bottom-2 flex justify-center text-zinc-400/80">
                Not affiliated with Mojang or Microsoft.
            </p>
        </div>
    </footer>
);
export default Footer;
