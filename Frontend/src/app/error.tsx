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
