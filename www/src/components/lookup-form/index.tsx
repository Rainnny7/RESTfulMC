"use client";

import PlatformSelectionDialog from "@/components/lookup-form/platform-selection-dialog";
import {
    InputGroup,
    InputGroupAddon,
    InputGroupInput,
} from "@/components/ui/input-group";
import { Spinner } from "@/components/ui/spinner";
import { isServerAddress } from "@/lib/string-utils";
import { cn } from "@/lib/utils";
import { SearchIcon } from "lucide-react";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";
import { useRouter } from "next/navigation";
import { ReactElement, SubmitEvent, useEffect, useState } from "react";
import { getPlayer, RestfulMCAPIError } from "restfulmc-lib";

type LookupFormProps = {
    className?: string | undefined;
    compact?: boolean;
    placeholder: string;
    defaultValue?: string | undefined;
    isFetching: boolean;
    error?: string | undefined;
    setIsFetching: (isFetching: boolean) => void;
    setError: (error: string | undefined) => void;
};

const LookupForm = ({
    className,
    compact,
    placeholder,
    defaultValue,
    isFetching,
    error,
    setIsFetching,
    setError,
}: LookupFormProps): ReactElement => {
    const router: AppRouterInstance = useRouter();
    const [platformDialogOpen, setPlatformDialogOpen] = useState(false);
    const [pendingServerQuery, setPendingServerQuery] = useState<string | null>(
        null
    );

    useEffect(() => {
        if (!error) return;
        const timeout = setTimeout(() => setError(undefined), 5000);
        return () => clearTimeout(timeout);
    }, [error, setError]);

    const handleSubmit = async (event: SubmitEvent<HTMLFormElement>) => {
        event.preventDefault();
        const query: string = (
            new FormData(event.currentTarget).get("query") as string
        ).trim();
        if (!query) return;

        // If the query is a server address, prompt the user to select a platform
        if (isServerAddress(query)) {
            setPendingServerQuery(query);
            setPlatformDialogOpen(true);
            return;
        }
        // Query for a player instead
        setIsFetching(true);
        setError(undefined);
        try {
            await getPlayer(query);
            router.push(`/player/${query}`);
        } catch (error) {
            const detailed: string | undefined =
                "message" in (error as unknown as RestfulMCAPIError)
                    ? (error as RestfulMCAPIError).message
                    : undefined;
            setError(detailed ?? "That player doesn't exist.");
        } finally {
            setIsFetching(false);
        }
    };

    return (
        <>
            <form className={cn(className)} onSubmit={handleSubmit}>
                <InputGroup
                    className={cn(
                        "transition-all duration-250 transform-gpu",
                        compact && "h-6.5",
                        error && "border-destructive"
                    )}
                >
                    <InputGroupAddon>
                        <SearchIcon
                            className={cn(
                                "transition-all duration-250 transform-gpu",
                                error && "text-destructive"
                            )}
                        />
                    </InputGroupAddon>
                    <InputGroupInput
                        className={cn(compact && "text-xs!")}
                        name="query"
                        type="search"
                        placeholder={placeholder}
                        disabled={isFetching}
                        defaultValue={defaultValue}
                    />
                    {isFetching && (
                        <InputGroupAddon align="inline-end">
                            <Spinner />
                        </InputGroupAddon>
                    )}
                </InputGroup>
            </form>

            {/* Platform selection dialog */}
            <PlatformSelectionDialog
                platformDialogOpen={platformDialogOpen}
                pendingServerQuery={pendingServerQuery}
                setIsFetching={setIsFetching}
                setPlatformDialogOpen={setPlatformDialogOpen}
                setPendingServerQuery={setPendingServerQuery}
                setError={setError}
            />
        </>
    );
};
export default LookupForm;
