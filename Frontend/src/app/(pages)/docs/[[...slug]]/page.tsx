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
import { getDocsContent } from "@/lib/mdx-utils";
import { PageProps } from "@/types/page";
import { notFound } from "next/navigation";
import moment from "moment";

import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { capitalize } from "@/lib/string-utils";
import { CustomMDX } from "@/components/mdx";
import Image from "next/image";
import { Metadata } from "next";
import Embed from "@/components/embed";
import SimpleTooltip from "@/components/simple-tooltip";
import SimpleLink from "@/components/simple-link";

/**
 * The page to display content
 * from an MDX file on the docs.
 *
 * @param params the params of the request
 * @return the page jsx
 */
const ContentPage = ({ params }: PageProps): ReactElement => {
    const slug: string = ((params.slug as string[]) || undefined)?.join("/"); // The slug of the content

    const content: DocsContentMetadata | undefined = getDocsContent().find(
        (metadata: DocsContentMetadata): boolean =>
            metadata.slug === (slug || "home")
    ); // Get the content to display based on the provided slug

    // Return a 404 if the content is not found
    if (!content) {
        notFound();
    }
    const splitSlug: string[] = content.slug?.split("/") || [];

    return (
        <main className="max-w-3xl xl:min-w-[52.5rem] 2xl:max-w-3xl px-3 xs:px-5 xl:px-7 flex flex-col gap-2 transition-all transform-gpu">
            {/* Header */}
            <div className="flex justify-between items-center gap-10">
                <div className="flex flex-col gap-3">
                    {/* Breadcrumb */}
                    <Breadcrumb>
                        <BreadcrumbList className="text-minecraft-green-4">
                            {["docs", ...splitSlug].map(
                                (part: string, index: number): ReactElement => (
                                    <div
                                        className="flex items-center"
                                        key={index}
                                    >
                                        <BreadcrumbItem>
                                            <BreadcrumbLink
                                                href={
                                                    (index > 0 ? "/docs" : "") +
                                                    `/${part}`
                                                }
                                            >
                                                {capitalize(part)}
                                            </BreadcrumbLink>
                                        </BreadcrumbItem>
                                        {index < splitSlug.length && (
                                            <BreadcrumbSeparator className="pl-1.5" />
                                        )}
                                    </div>
                                )
                            )}
                        </BreadcrumbList>
                    </Breadcrumb>

                    {/* Publish Date */}
                    <p className="text-zinc-400 select-none pointer-events-none">
                        Published on{" "}
                        {moment(content.published, "MM-DD-YYYY").format(
                            "MMMM Do YYYY"
                        )}
                    </p>
                </div>

                {/* View on Git */}
                <SimpleTooltip content="Click to view on Git">
                    <SimpleLink
                        className="hover:opacity-85 transition-all transform-gpu"
                        href={`https://git.rainnny.club/Rainnny/RESTfulMC/src/branch/master/Frontend/docs/${content.slug}${content.extension}`}
                        noRef
                        newTab
                    >
                        <Image
                            src="/media/github-white-logo.svg"
                            alt="GitHub Logo"
                            width={26}
                            height={26}
                        />
                    </SimpleLink>
                </SimpleTooltip>
            </div>

            {/* Content */}
            <CustomMDX source={content.content} />
        </main>
    );
};

/**
 * Generate metadata for this page.
 *
 * @param params the route params
 * @returns the generated metadata
 */
export const generateMetadata = async ({
    params,
}: PageProps): Promise<Metadata> => {
    const slug: string = ((params.slug as string[]) || undefined)?.join("/"); // The slug of the content
    let embed: Metadata | undefined; // The content embed, if any
    if (slug) {
        const content: DocsContentMetadata | undefined = getDocsContent().find(
            (metadata: DocsContentMetadata): boolean => metadata.slug === slug
        ); // Get the content based on the provided slug
        if (content) {
            return Embed({
                title: content.title,
                description: content.summary,
            });
        }
    }

    // Return the page embed
    return embed
        ? embed
        : Embed({
              title: "Documentation",
              description:
                  "Need help with RESTfulMC? You've come to the right place!",
          });
};

export default ContentPage;
