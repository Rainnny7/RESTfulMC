import { NextRequest, NextResponse } from "next/server";
import { DOCS_SEARCH_INDEX } from "@/lib/search";
import { FuseResult } from "fuse.js";

export const GET = async (request: NextRequest): Promise<NextResponse> => {
    const query: string | null = request.nextUrl.searchParams.get("query"); // The query to search for

    // Ensure the query is valid
    if (!query || query.length < 3 || query.length > 64) {
        return new NextResponse(
            JSON.stringify({ error: "Invalid query given" }),
            { status: 400 }
        );
    }

    // Return the results of the search
    return new NextResponse(
        JSON.stringify(
            DOCS_SEARCH_INDEX.search(query, { limit: 5 }).map(
                (result: FuseResult<DocsContentMetadata>) => {
                    return {
                        slug: result.item.slug,
                        title: result.item.title,
                        summary: result.item.summary,
                    };
                }
            )
        )
    );
};
