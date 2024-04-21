"use client";

import { ReactElement, useEffect, useState } from "react";
import {
    CommandDialog,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import { Input } from "@/components/ui/input";
import { useRouter } from "next/navigation";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";

/**
 * The dialog for quickly searching the docs.
 *
 * @return the content jsx
 */
const QuickSearchDialog = (): ReactElement => {
    const [open, setOpen] = useState<boolean>(false);
    const [results, setResults] = useState<DocsContentMetadata[] | undefined>(
        undefined
    ); // The search results
    const router: AppRouterInstance = useRouter();

    // Fetch the default results when the page loads
    useEffect((): void => {
        if (!results) {
            const fetchDefaults = async () => {
                const response: Response = await fetch("/api/docs/search"); // Search the docs
                // setLabel(undefined); // Clear the label
                setResults((await response.json()) as DocsContentMetadata[]);
            };
            fetchDefaults();
        }
    }, [results]);

    // Render the contents
    return (
        <>
            {/* Button to open */}
            <div onClick={() => setOpen(true)}>
                <div className="absolute top-2.5 left-3 z-10">
                    <MagnifyingGlassIcon
                        className="absolute"
                        width={22}
                        height={22}
                    />
                </div>

                <Input
                    className="pl-10"
                    type="search"
                    name="search"
                    placeholder="Quick search..."
                    readOnly
                />
            </div>

            {/* Dialog */}
            <CommandDialog open={open} onOpenChange={setOpen}>
                {/* Input */}
                <CommandInput placeholder="Start typing to get started..." />

                {/* Results */}
                <CommandList>
                    <CommandEmpty className="text-center text-red-500">
                        No results were found.
                    </CommandEmpty>

                    <CommandGroup heading="Results">
                        {results?.map(
                            (
                                result: DocsContentMetadata,
                                index: number
                            ): ReactElement => (
                                <CommandItem
                                    key={index}
                                    className="flex flex-col gap-1 items-start"
                                    onSelect={() =>
                                        router.push(`/docs/${result.slug}`)
                                    }
                                >
                                    <h1 className="text-minecraft-green-3 font-bold">
                                        {result.title}
                                    </h1>
                                    <p className="text-zinc-300/85">
                                        {result.summary}
                                    </p>
                                </CommandItem>
                            )
                        )}
                    </CommandGroup>
                </CommandList>
            </CommandDialog>
        </>
    );
};

export default QuickSearchDialog;
