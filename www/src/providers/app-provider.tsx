"use client";

import { ThemeProvider } from "@/providers/theme-provider";
import NextTopLoader from "nextjs-toploader";
import { ReactNode } from "react";

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
        {children}
    </ThemeProvider>
);
