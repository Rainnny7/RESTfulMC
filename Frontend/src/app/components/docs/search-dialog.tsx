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

    // Listen for CTRL + K keybinds to open this dialog
    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent): void => {
            if ((event.ctrlKey || event.metaKey) && event.key === "k") {
                event.preventDefault();
                setOpen((open: boolean) => !open);
            }
        };
        document.addEventListener("keydown", handleKeyDown);
        return () => document.removeEventListener("keydown", handleKeyDown);
    }, []);

    // Render the contents
    return (
        <>
            {/* Button to open */}
            <div
                className="hover:opacity-85 transition-all transform-gpu"
                onClick={() => setOpen(true)}
            >
                <div className="absolute top-2.5 left-3 z-10">
                    <MagnifyingGlassIcon
                        className="absolute"
                        width={22}
                        height={22}
                    />
                </div>

                <Input
                    className="pl-10 cursor-pointer"
                    type="search"
                    name="search"
                    placeholder="Quick search..."
                    readOnly
                />

                <div className="absolute top-1.5 right-3">
                    <kbd className="h-5 px-1.5 inline-flex gap-1 items-center bg-muted font-medium text-muted-foreground rounded select-none pointer-events-none">
                        <span>⌘</span>K
                    </kbd>
                </div>
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
