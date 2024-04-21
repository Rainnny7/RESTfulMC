import { ReactElement } from "react";
import { getDocsContent } from "@/lib/mdxUtils";
import { PageProps } from "@/types/page";
import { notFound } from "next/navigation";

import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { capitalize } from "@/lib/stringUtils";
import { CustomMDX } from "@/mdx-components";

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
        <main>
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

            {/* Content */}
            <CustomMDX source={content.content} />
        </main>
    );
};
export default ContentPage;
