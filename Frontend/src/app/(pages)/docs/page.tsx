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
    <main className="h-[64vh] flex flex-col gap-3 justify-center items-center">
        {/* Creeper */}
        <div className="absolute left-28 bottom-16 pointer-events-none">
            <Creeper />
        </div>

        {/* Header */}
        <h1
            className={cn(
                "text-6xl text-minecraft-green-3 pointer-events-none",
                minecrafter.className
            )}
        >
            Documentation
        </h1>

        {/* Content */}
        <h2 className="text-xl">
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
