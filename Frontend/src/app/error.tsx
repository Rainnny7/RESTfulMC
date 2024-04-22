"use client";

import Creeper from "@/components/creeper";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import { ReactElement, useEffect } from "react";
import MinecraftButton from "@/components/button/minecraft-button";

/**
 * The error page.
 *
 * @returns the page jsx
 */
const ErrorPage = ({
    error,
    reset,
}: {
    error: Error & { digest?: string };
    reset: () => void;
}): ReactElement => {
    // Log the error upon mount
    useEffect((): void => {
        console.error(error);
    }, [error]);

    // Render the page
    return (
        <main className="h-screen flex flex-col gap-3 justify-center items-center text-center pointer-events-none">
            {/* Creeper */}
            <Creeper />

            {/* Header */}
            <h1
                className={cn(
                    "text-5xl sm:text-6xl text-minecraft-green-3",
                    minecrafter.className
                )}
            >
                We&apos;re Sssssorry
            </h1>

            {/* Error */}
            <h2 className="text-2xl">
                It&apos;s not you, it&apos;s us, something went wrong.
            </h2>

            {/* Try Again */}
            <MinecraftButton className="mt-3.5 h-11" onClick={() => reset()}>
                Try Again?
            </MinecraftButton>
        </main>
    );
};
export default ErrorPage;
