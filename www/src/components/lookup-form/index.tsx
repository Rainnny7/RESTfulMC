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
import { getPlayer } from "restfulmc-lib";

type LookupFormProps = {
    className?: string | undefined;
    placeholder: string;
    error?: string | undefined;
    setError: (error: string | undefined) => void;
};

const LookupForm = ({
    className,
    error,
    placeholder,
    setError,
}: LookupFormProps): ReactElement => {
    const router: AppRouterInstance = useRouter();
    const [isFetching, setIsFetching] = useState<boolean>(false);

    const [platformDialogOpen, setPlatformDialogOpen] = useState(false);

    useEffect(() => {
        if (!error) return;
        const timeout = setTimeout(() => setError(undefined), 5000);
        return () => clearTimeout(timeout);
    }, [error, setError]);
    const [pendingServerQuery, setPendingServerQuery] = useState<string | null>(
        null
    );

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
            router.push(`/player/${(await getPlayer(query)).username}`);
        } catch {
            setError("That player doesn't exist.");
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
                        name="query"
                        type="search"
                        placeholder={placeholder}
                        disabled={isFetching}
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
