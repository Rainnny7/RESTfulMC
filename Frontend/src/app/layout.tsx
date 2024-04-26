/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import Footer from "@/components/footer";
import Navbar from "@/components/navbar";
import { Toaster } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import config from "@/config";
import { notoSans } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import type { Metadata, Viewport } from "next";
import PlausibleProvider from "next-plausible";
import { ReactElement, ReactNode } from "react";
import "./globals.css";
import ThemeProvider from "@/provider/theme-provider";

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
                        <Footer />
                    </TooltipProvider>

                    {/* Toasts */}
                    <Toaster />
                </ThemeProvider>
            </body>
        </html>
    );
};
export default RootLayout;
