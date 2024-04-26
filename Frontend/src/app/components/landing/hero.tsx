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
import GitHubStarButton from "@/components/button/github-star-button";
import MinecraftButton from "@/components/button/minecraft-button";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import { ReactElement } from "react";
import SimpleLink from "@/components/simple-link";

/**
 * The hero content.
 *
 * @returns the hero jsx
 */
const Hero = (): ReactElement => (
    <div className="pt-56 pb-40 flex flex-col gap-8 justify-center items-center">
        <div className="flex flex-col gap-4 items-center text-center select-none pointer-events-none">
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
        <div className="flex gap-3.5 xs:gap-5 sm:gap-10">
            <SimpleLink href="/docs">
                <MinecraftButton className="w-36 xs:w-44 h-12">
                    Get Started
                </MinecraftButton>
            </SimpleLink>

            {/* Star on Github <3 */}
            <div className="md:hidden">
                <GitHubStarButton />
            </div>
        </div>
    </div>
);
export default Hero;
