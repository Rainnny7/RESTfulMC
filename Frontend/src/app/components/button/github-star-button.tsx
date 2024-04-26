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

import MinecraftButton from "@/components/button/minecraft-button";
import { Skeleton } from "@/components/ui/skeleton";
import { StarIcon } from "@heroicons/react/24/outline";
import {
    Dispatch,
    ReactElement,
    SetStateAction,
    Suspense,
    useEffect,
    useState,
} from "react";
import SimpleLink from "@/components/simple-link";

/**
 * The button to display the amount
 * of stars the GitHub repository has.
 *
 * @returns the component jsx
 */
const GitHubStarButton = (): ReactElement => {
    const [stars, setStars]: [
        number | undefined,
        Dispatch<SetStateAction<number | undefined>>,
    ] = useState<number | undefined>(undefined); // The stars of the repository

    useEffect(() => {
        const fetchStars = async (): Promise<void> => {
            const response: Response = await fetch(
                "https://api.github.com/repos/Rainnny7/RESTfulMC",
                { next: { revalidate: 300 } } // Revalidate every 5 minutes
            );
            const json: any = await response.json(); // Get the JSON response
            setStars(json.stargazers_count); // Set the stars
        };
        if (stars === undefined) {
            fetchStars(); // Fetch the stars
        }
    }, [stars]);

    return (
        <SimpleLink href="https://github.com/Rainnny7/RESTfulMC" noRef newTab>
            <MinecraftButton className="px-3 xs:px-5 flex gap-1.5 items-center group/star">
                {/* Star Count */}
                <Suspense
                    fallback={<Skeleton className="w-4 h-5 rounded-md" />}
                >
                    <code className="px-1 rounded-md bg-minecraft-green-3/80">
                        {stars || 0}
                    </code>
                </Suspense>

                <StarIcon
                    className="group-hover/star:text-orange-400 delay-0 transition-all transform-gpu"
                    width={22}
                    height={22}
                />
                <span>Star on GitHub</span>
            </MinecraftButton>
        </SimpleLink>
    );
};

export default GitHubStarButton;
