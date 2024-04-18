import Navbar from "@/components/navbar";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import config from "@/config";
import { notoSans } from "@/font/fonts";
import { cn } from "@/lib/utils";
import ThemeProvider from "@/provider/theme-provider";
import type { Metadata, Viewport } from "next";
import PlausibleProvider from "next-plausible";
import { ReactElement, ReactNode } from "react";
import "./globals.css";

/**
 * Site metadata & viewport.
 */
export const metadata: Metadata = config.metadata;
export const viewport: Viewport = config.viewport;

/**
 * The root layout for this site.
 *
 * @param children the children of this layout
 * @returns the layout jsx
 */
const RootLayout = ({
    children,
}: Readonly<{
    children: ReactNode;
}>): ReactElement => {
    const analyticsDomain: string | undefined = config.analyticsDomain;
    return (
        <html lang="en" className={cn("scroll-smooth", notoSans.className)}>
            <head>
                {analyticsDomain && (
                    <PlausibleProvider
                        domain={analyticsDomain}
                        customDomain="https://analytics.rainnny.club"
                        selfHosted
                    />
                )}
            </head>
            <body className="relative min-h-screen">
                <ThemeProvider attribute="class" defaultTheme="dark">
                    <TooltipProvider>
                        <Navbar />
                        {children}
                        {/*<Footer />*/}
                    </TooltipProvider>

                    {/* Toasts */}
                    <Toaster />
                </ThemeProvider>
            </body>
        </html>
    );
};
export default RootLayout;
