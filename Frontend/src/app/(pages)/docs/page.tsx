import Creeper from "@/components/creeper";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import { Metadata } from "next";
import Link from "next/link";
import { ReactElement } from "react";

/**
 * Page metadata.
 */
export const metadata: Metadata = {
    title: "Docs",
};

/**
 * The documentation page.
 *
 * @returns the page jsx
 */
const DocsPage = (): ReactElement => (
    <main className="h-[84vh] flex flex-col gap-3 justify-center items-center text-center pointer-events-none">
        {/* Creeper */}
        <Creeper />

        {/* Header */}
        <h1
            className={cn(
                "text-5xl sm:text-6xl text-minecraft-green-3",
                minecrafter.className
            )}
        >
            Documentation
        </h1>

        {/* Error */}
        <h2 className="text-2xl">
            This page is still under construction, however we do have a{" "}
            <Link
                className="text-minecraft-green-4"
                href="https://git.rainnny.club/Rainnny/RESTfulMC/wiki"
            >
                Wiki
            </Link>
            !
        </h2>
    </main>
);
export default DocsPage;
