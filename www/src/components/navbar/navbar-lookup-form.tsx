"use client";

import LookupForm from "@/components/lookup-form";
import { usePathname } from "next/navigation";
import { ReactElement, useState } from "react";

const NavbarLookupForm = (): ReactElement => {
    const pathname = usePathname();
    const [isLookupFormFetching, setIsLookupFormFetching] = useState(false);
    const [lookupError, setLookupError] = useState<string>();
    const matchedPath: RegExpMatchArray | null = pathname?.match(
        /^\/(?:player|server\/(?:java|bedrock))\/(.+)$/
    );

    return (
        <div className="relative">
            <LookupForm
                className="w-32 sm:w-46"
                placeholder="Player / Server Lookup"
                compact
                isFetching={isLookupFormFetching}
                error={lookupError}
                defaultValue={
                    matchedPath
                        ? decodeURIComponent(matchedPath[1]) || undefined
                        : undefined
                }
                setIsFetching={setIsLookupFormFetching}
                setError={setLookupError}
            />
            {lookupError && (
                <div className="absolute left-0 top-full mt-2 px-2 py-1 bg-destructive/70 backdrop-blur-sm text-destructive-foreground text-sm rounded-md">
                    {lookupError}
                </div>
            )}
        </div>
    );
};
export default NavbarLookupForm;
