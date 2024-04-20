/**
 * Metadata for documentation content.
 */
type DocsContentMetadata = MDXMetadata & {
    /**
     * The title of this content.
     */
    title: string;

    /**
     * The date this content was published.
     */
    published: string;

    /**
     * The summary of this content.
     */
    summary: string;
};

/**
 * Metadata for an MDX file.
 */
type MDXMetadata = {
    /**
     * The slug of the file, defined once read.
     */
    slug?: string | undefined;

    /**
     * The metadata of the file.
     */
    metadata: {
        [key: string]: string;
    };

    /**
     * The content of the file.
     */
    content: string;
};
