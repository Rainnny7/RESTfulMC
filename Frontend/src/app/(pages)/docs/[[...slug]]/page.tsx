import { ReactElement } from "react";
import { getDocsContent } from "@/lib/mdxUtils";
import { PageProps } from "@/types/page";
import { notFound } from "next/navigation";

/**
 * The page to display content
 * from an MDX file on the docs.
 *
 * @param slug the slug of the mdx file
 * @return the page jsx
 */
const ContentPage = ({ params: { slug } }: PageProps): ReactElement => {
    const content: DocsContentMetadata | undefined = getDocsContent().find(
        (metadata: DocsContentMetadata): boolean =>
            metadata.slug === (slug ? slug[0] : "home")
    ); // Get the content to display based on the provided slug

    // Return a 404 if the content is not found
    if (!content) {
        notFound();
    }

    return <main>{content.title}</main>;
};
export default ContentPage;
