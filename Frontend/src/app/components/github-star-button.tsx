import MinecraftButton from "@/components/minecraft-button";
import { Skeleton } from "@/components/ui/skeleton";
import { StarIcon } from "@heroicons/react/24/outline";
import Link from "next/link";
import { ReactElement, Suspense } from "react";

/**
 * The button to display the amount
 * of stars the GitHub repository has.
 *
 * @returns the component jsx
 */
const GitHubStarButton = async (): Promise<ReactElement> => {
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
                    <GitHubStarCount />
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

/**
 * The github star count component.
 *
 * @returns the star count jsx
 */
const GitHubStarCount = async (): Promise<ReactElement> => {
    const stars: number = await getStarCount(); // Get the repo star count
    return (
        <code className="px-1 rounded-md bg-minecraft-green-3/80">{stars}</code>
    );
};

/**
 * Get the amount of stars
 * the repository has.
 *
 * @returns the star count
 */
const getStarCount = async (): Promise<number> => {
    const response: Response = await fetch(
        "https://api.github.com/repos/Rainnny7/RESTfulMC",
        { next: { revalidate: 300 } } // Revalidate every 5 minutes
    );
    const json: any = await response.json(); // Get the JSON response
    return json.stargazers_count; // Return the stars
};

export default GitHubStarButton;
