"use client";

import LookupForm from "@/components/lookup-form";
import { ReactElement, useState } from "react";

const NavbarLookupForm = (): ReactElement => {
    const [isLookupFormFetching, setIsLookupFormFetching] =
        useState<boolean>(false);
    const [lookupError, setLookupError] = useState<string | undefined>(
        undefined
    );
    return (
        <div className="relative">
            <LookupForm
                className="min-w-50"
                placeholder="Player / Server Lookup"
                compact
                isFetching={isLookupFormFetching}
                error={lookupError}
                setIsFetching={setIsLookupFormFetching}
                setError={setLookupError}
            />

            {lookupError && (
                <div className="absolute left-0 -bottom-9">
                    <div className="px-2 py-1 bg-destructive/70 backdrop-blur-sm text-destructive-foreground text-sm rounded-md">
                        {lookupError}
                    </div>
                </div>
            )}
        </div>
    );
};
export default NavbarLookupForm;
