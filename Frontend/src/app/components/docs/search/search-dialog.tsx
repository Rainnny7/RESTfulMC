"use client";

import {
    AnchorHTMLAttributes,
    ChangeEvent,
    HTMLAttributes,
    ReactElement,
    useState,
} from "react";
import {
    DialogClose,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import Link from "next/link";

/**
 * Content for the search dialog.
 *
 * @return the content jsx
 */
const SearchDialogContent = (): ReactElement => {
    const [label, setLabel] = useState<string | undefined>(undefined);
    const [results, setResults] = useState<DocsContentMetadata[] | undefined>(
        undefined
    ); // The search results

    /**
     * Search the docs with the given query.
     */
    const search = async (
        event: ChangeEvent<HTMLInputElement>
    ): Promise<void> => {
        const query: string = event.target.value; // Get the query to search for
        const tooShort: boolean = query.length < 3;

        // No query or too short
        if (!query || tooShort || query.length > 64) {
            // Display warning
            if (query) {
                setLabel(
                    tooShort
                        ? "Please enter at least 3 characters"
                        : "Your input is too long"
                );
            }
            setResults(undefined);
            return;
        }
        const response: Response = await fetch(
            `/api/docs/search?query=${query}`
        ); // Search the docs
        setLabel(undefined); // Clear the label
        setResults((await response.json()) as DocsContentMetadata[]);
    };

    // Render the contents
    return (
        <>
            {/* Header */}
            <DialogHeader className="flex flex-col gap-2">
                <DialogTitle>Quick Search</DialogTitle>
                <DialogDescription>
                    Quickly find the documentation you&apos;re looking for by
                    typing in a few terms.
                </DialogDescription>

                {/* Query Input */}
                <div className="space-y-1.5">
                    <Label htmlFor="search">
                        {label || "Start typing to get started"}
                    </Label>
                    <Input
                        type="search"
                        name="search"
                        placeholder="Query..."
                        onChange={search}
                    />
                </div>
            </DialogHeader>

            {/* Results */}
            <div className="flex flex-col gap-2">
                {results?.length === 0 && (
                    <p className="text-red-500">No Results</p>
                )}
                {results?.map(
                    (
                        result: DocsContentMetadata,
                        index: number
                    ): ReactElement => (
                        <SearchResultEntry key={index} result={result} />
                    )
                )}
            </div>

            {/* Footer */}
            <DialogFooter className="sm:justify-start">
                <DialogClose asChild>
                    <Button type="button" variant="secondary">
                        Close
                    </Button>
                </DialogClose>
            </DialogFooter>
        </>
    );
};

/**
 * The props for a search result entry.
 */
type SearchResultEntryProps = {
    /**
     * The search result to display.
     */
    result: DocsContentMetadata;
};

/**
 * A search result entry.
 *
 * @param result the result to display
 * @param props the additional props
 * @return the result jsx
 */
const SearchResultEntry = ({
    result,
    ...props
}: AnchorHTMLAttributes<HTMLAnchorElement> &
    SearchResultEntryProps): ReactElement => (
    <Link
        className="p-3 flex flex-col gap-1.5 bg-muted rounded-xl"
        href={`/docs/${result.slug}`}
        {...props}
    >
        <h1 className="font-semibold text-minecraft-green-4">{result.title}</h1>
        <p className="font-light text-zinc-200">{result.summary}</p>
    </Link>
);

export default SearchDialogContent;
