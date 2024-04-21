import { ReactElement } from "react";
import Link from "next/link";
import { cn } from "@/lib/utils";
import { capitalize } from "@/lib/stringUtils";
import { minecrafter } from "@/font/fonts";
import { getDocsContent } from "@/lib/mdxUtils";
import QuickSearchDialog from "@/components/docs/search/search-dialog";

/**
 * The sidebar for the docs page.
 *
 * @returns the sidebar jsx
 */
const Sidebar = ({ activeSlug }: { activeSlug: string }): ReactElement => {
    const groupedContent: Record<string, DocsContentMetadata[]> = {};
    for (let content of getDocsContent()) {
        const categoryKey: string = content.slug?.split("/")[0] || "home"; // The key of the category
        if (!groupedContent[categoryKey]) {
            groupedContent[categoryKey] = [];
        }
        groupedContent[categoryKey].push(content);
    }
    return (
        <div className="hidden h-full px-3 py-5 xl:flex flex-col items-center">
            <div className="fixed w-56 flex flex-col gap-5">
                {/* Quick Search */}
                <QuickSearchDialog />

                {/* Links */}
                <div className="flex flex-col gap-7">
                    {Object.entries(groupedContent).map(
                        (
                            [category, categoryContent]: [
                                string,
                                DocsContentMetadata[],
                            ],
                            index: number
                        ): ReactElement => (
                            <div key={index} className="flex flex-col gap-1.5">
                                {/* Category */}
                                <h1
                                    className={cn(
                                        "text-xl text-minecraft-green-4 select-none pointer-events-none",
                                        minecrafter.className
                                    )}
                                >
                                    {capitalize(category)}
                                </h1>

                                {/* Links */}
                                <div className="flex flex-col border-l border-zinc-700">
                                    {categoryContent.map(
                                        (
                                            content: DocsContentMetadata,
                                            contentIndex: number
                                        ): ReactElement => {
                                            const active: boolean =
                                                (!activeSlug &&
                                                    content.slug === "home") ||
                                                activeSlug === content.slug;
                                            return (
                                                <Link
                                                    key={contentIndex}
                                                    className={cn(
                                                        "pl-3 -ml-px text-zinc-200 hover:opacity-85 transition-all transform-gpu",
                                                        active &&
                                                            "border-l border-minecraft-green-4"
                                                    )}
                                                    href={
                                                        `/docs/${content.slug}` ||
                                                        "#"
                                                    }
                                                >
                                                    {content.title}
                                                </Link>
                                            );
                                        }
                                    )}
                                </div>
                            </div>
                        )
                    )}
                </div>
            </div>
        </div>
    );
};
export default Sidebar;
