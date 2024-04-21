import Fuse from "fuse.js";
import { getDocsContent } from "@/lib/mdxUtils";

/**
 * The fuse index for searching the docs.
 */
export const DOCS_SEARCH_INDEX: Fuse<DocsContentMetadata> = new Fuse(
    getDocsContent(),
    {
        keys: ["title", "summary"],
        includeScore: true,
        threshold: 0.5,
    }
);
