import { ReactElement } from "react";
import { getDocsContent } from "@/lib/mdxUtils";
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
import { capitalize } from "@/lib/stringUtils";
import { CustomMDX } from "@/components/mdx";
import Link from "next/link";
import Image from "next/image";

/**
 * The page to display content
 * from an MDX file on the docs.
 *
 * @param slug the slug of the mdx file
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
        <main className="relative pl-7 flex flex-col gap-3.5">
            {/* Breadcrumb */}
            <Breadcrumb>
                <BreadcrumbList className="text-minecraft-green-4">
                    {["docs", ...splitSlug].map(
                        (part: string, index: number): ReactElement => (
                            <div className="flex items-center" key={index}>
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
            <p className="text-zinc-400 pointer-events-none">
                Published on{" "}
                {moment(content.published, "MM-DD-YYYY").format("MMMM Do YYYY")}
            </p>
            {/* Content */}
            <CustomMDX source={content.content} />

            {/* View on Git */}
            <Link
                className="absolute left-10 bottom-7"
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
        </main>
    );
};
export default ContentPage;
