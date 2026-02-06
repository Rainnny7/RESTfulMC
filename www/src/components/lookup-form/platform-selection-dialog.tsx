"use client";

import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import Image from "next/image";
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
            <DialogContent className="sm:max-w-lg">
                <DialogHeader>
                    <DialogTitle>Select platform</DialogTitle>
                    <DialogDescription>
                        Choose the Minecraft edition for{" "}
                        <span className="font-medium text-foreground">
                            {pendingServerQuery}
                        </span>
                    </DialogDescription>
                </DialogHeader>
                <div className="grid grid-cols-2 gap-3">
                    <PlatformSelectionCard
                        platform={ServerPlatform.JAVA}
                        title="Java Edition"
                        description="PC, Mac & Linux — the original Minecraft experience"
                        handlePlatformSelect={handlePlatformSelect}
                    />
                    <PlatformSelectionCard
                        platform={ServerPlatform.BEDROCK}
                        title="Bedrock Edition"
                        description="Xbox, PlayStation, Switch & mobile — cross-play enabled"
                        handlePlatformSelect={handlePlatformSelect}
                    />
                </div>
            </DialogContent>
        </Dialog>
    );
};

const PlatformSelectionCard = ({
    platform,
    title,
    description,
    handlePlatformSelect,
}: {
    platform: ServerPlatform;
    title: string;
    description: string;
    handlePlatformSelect: (platform: ServerPlatform) => void;
}) => {
    return (
        <button
            className="group relative flex min-h-[140px] flex-col items-center justify-end overflow-hidden rounded-xl border border-border p-4 text-left transition-all hover:border-foreground/20 hover:ring-2 hover:ring-foreground/10 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
            type="button"
            onClick={() => handlePlatformSelect(platform)}
        >
            <Image
                className="object-cover opacity-80 transition-opacity group-hover:opacity-90"
                src={`/media/background/${platform.toLowerCase()}-edition.webp`}
                alt={`${platform} edition background`}
                fill
            />
            <div className="absolute inset-0 bg-linear-to-t from-black/80 via-black/40 to-transparent" />
            <div className="relative z-10 flex flex-col gap-1 text-white [&_span:last-child]:text-white/90 [&_span]:drop-shadow-[0_1px_2px_rgba(0,0,0,0.8)]">
                <span className="text-lg font-bold">{title}</span>
                <span className="text-xs">{description}</span>
            </div>
        </button>
    );
};

export default PlatformSelectionDialog;
