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
import Link from "next/link";
import Image from "next/image";
import { Metadata } from "next";
import Embed from "@/components/embed";
import SimpleTooltip from "@/components/simple-tooltip";

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
                    <Link
                        className="hover:opacity-85 transition-all transform-gpu"
                        href={`https://git.rainnny.club/Rainnny/RESTfulMC/src/branch/master/Frontend/docs/${content.slug}${content.extension}`}
                        rel="noopener noreferrer"
                        target="_blank"
                    >
                        <Image
                            src="/media/github-white-logo.svg"
                            alt="GitHub Logo"
                            width={26}
                            height={26}
                        />
                    </Link>
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
