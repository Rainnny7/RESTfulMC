"use client";

import { Toaster } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import NextTopLoader from "nextjs-toploader";
import { ReactNode } from "react";

const queryClient = new QueryClient();

export const AppProvider = ({
    children,
}: Readonly<{
    children: ReactNode;
}>) => (
    <>
        <NextTopLoader
            color="var(--color-toploader-color)"
            showSpinner={false}
        />
        <TooltipProvider delayDuration={100}>
            <QueryClientProvider client={queryClient}>
                {children}
                <Toaster
                    position="bottom-right"
                    toastOptions={{
                        duration: 3000,
                        classNames: {
                            toast: "!flex !items-center !gap-2 !bg-popover/50 !backdrop-blur-sm !border !border-border !rounded-xl",
                            success: "!text-green-500",
                            error: "!text-red-500",
                            content: "!text-white/95",
                        },
                    }}
                />
            </QueryClientProvider>
        </TooltipProvider>
    </>
);
