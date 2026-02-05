import { cn } from "@/lib/utils";
import { AppProvider } from "@/providers/app-provider";
import type { Metadata, Viewport } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import { ReactNode } from "react";
import "./styles/globals.css";

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

export const metadata: Metadata = {
    title: {
        default: "RESTfulMC",
        template: `RESTfulMC • %s`,
    },
    description:
        "🌐 A simple, yet useful RESTful API for Minecraft utilizing Springboot.",
    openGraph: {
        type: "website",
    },
    twitter: { card: "summary_large_image" },
};
export const viewport: Viewport = { themeColor: "#6468f0" };

const RootLayout = ({
    children,
}: Readonly<{
    children: ReactNode;
}>) => (
    <html lang="en" suppressHydrationWarning>
        <body
            className={cn(
                "antialiased scroll-smooth select-none",
                geistSans.variable,
                geistMono.variable
            )}
            style={{
                background:
                    "linear-gradient(to top, var(--alternative-background), var(--background))",
            }}
        >
            <AppProvider>{children}</AppProvider>
        </body>
    </html>
);
export default RootLayout;
