"use client";

import { Button } from "@/components/ui/button";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import { ReactElement } from "react";
import { getMinecraftServer, ServerPlatform } from "restfulmc-lib";

type PlatformSelectionDialogProps = {
    platformDialogOpen: boolean;
    pendingServerQuery: string | null;
    setIsFetching: (fetching: boolean) => void;
    setPlatformDialogOpen: (open: boolean) => void;
    setPendingServerQuery: (query: string | null) => void;
    setError: (error: string | undefined) => void;
};

const PlatformSelectionDialog = ({
    platformDialogOpen,
    pendingServerQuery,
    setIsFetching,
    setPlatformDialogOpen,
    setPendingServerQuery,
    setError,
}: PlatformSelectionDialogProps): ReactElement => {
    const handlePlatformSelect = async (platform: ServerPlatform) => {
        if (!pendingServerQuery) return;
        setPlatformDialogOpen(false);
        setIsFetching(true);
        setError(undefined);
        try {
            const server = await getMinecraftServer(
                platform,
                pendingServerQuery
            );
            console.log({ server });
        } catch {
            setError("Failed to lookup server.");
        } finally {
            setIsFetching(false);
            setPendingServerQuery(null);
        }
    };

    return (
        <Dialog
            open={platformDialogOpen}
            onOpenChange={(open) => {
                setPlatformDialogOpen(open);
                if (!open) setPendingServerQuery(null);
            }}
        >
            <DialogContent showCloseButton>
                <DialogHeader>
                    <DialogTitle>Select platform</DialogTitle>
                    <DialogDescription>
                        Choose the Minecraft edition for{" "}
                        <span className="font-medium text-foreground">
                            {pendingServerQuery}
                        </span>
                    </DialogDescription>
                </DialogHeader>
                <DialogFooter>
                    <Button
                        variant="outline"
                        onClick={() =>
                            handlePlatformSelect(ServerPlatform.JAVA)
                        }
                    >
                        Java
                    </Button>
                    <Button
                        variant="outline"
                        onClick={() =>
                            handlePlatformSelect(ServerPlatform.BEDROCK)
                        }
                    >
                        Bedrock
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};
export default PlatformSelectionDialog;