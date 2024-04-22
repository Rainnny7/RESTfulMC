import { NextRequest, NextResponse } from "next/server";
import { getDocsContent } from "@/lib/mdx-utils";

export const GET = async (request: NextRequest): Promise<NextResponse> => {
    return new NextResponse(JSON.stringify(getDocsContent()));
};
