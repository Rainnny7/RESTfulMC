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
import { ReactElement } from "react";
import { cn } from "@/lib/utils";
import { capitalize } from "@/lib/string-utils";
import { minecrafter } from "@/font/fonts";
import { getDocsContent } from "@/lib/mdx-utils";
import QuickSearchDialog from "@/components/docs/search-dialog";
import SimpleLink from "@/components/simple-link";

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
        <div className="hidden h-full px-3 pb-5 xl:flex flex-wrap flex-col items-center">
            <div className="fixed w-52 2xl:w-56 flex flex-col gap-5 transition-all transform-gpu">
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
                                                <SimpleLink
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
                                                </SimpleLink>
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
