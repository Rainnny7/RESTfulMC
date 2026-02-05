"use client";

import { TooltipProvider } from "@/components/ui/tooltip";
import { ThemeProvider } from "@/providers/theme-provider";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import NextTopLoader from "nextjs-toploader";
import { ReactNode } from "react";

const queryClient = new QueryClient();

export const AppProvider = ({
    children,
}: Readonly<{
    children: ReactNode;
}>) => (
    <ThemeProvider attribute="class" defaultTheme="dark" enableSystem>
        <NextTopLoader
            color="var(--color-toploader-color)"
            showSpinner={false}
        />
        <TooltipProvider delayDuration={100}>
            <QueryClientProvider client={queryClient}>
                {children}
            </QueryClientProvider>
        </TooltipProvider>
    </ThemeProvider>
);
