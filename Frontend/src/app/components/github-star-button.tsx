"use client";

import MinecraftButton from "@/components/minecraft-button";
import { Skeleton } from "@/components/ui/skeleton";
import { StarIcon } from "@heroicons/react/24/outline";
import Link from "next/link";
import {
    Dispatch,
    ReactElement,
    SetStateAction,
    Suspense,
    useEffect,
    useState,
} from "react";

/**
 * The button to display the amount
 * of stars the GitHub repository has.
 *
 * @returns the component jsx
 */
const GitHubStarButton = (): ReactElement => {
    const [stars, setStars]: [
        number | undefined,
        Dispatch<SetStateAction<number | undefined>>
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
        <Link
            href="https://github.com/Rainnny7/RESTfulMC"
            rel="noopener noreferrer"
            target="_blank"
        >
            <MinecraftButton className="flex gap-1.5 items-center group/star">
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
        </Link>
    );
};

export default GitHubStarButton;
