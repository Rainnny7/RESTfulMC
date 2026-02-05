import { env } from "@/lib/env";
import { cn } from "@/lib/utils";
import { ThemeProvider } from "@/provider/theme-provider";
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
        template: `%s • RESTfulMC`,
    },
    description:
        "🌐 A simple, yet useful RESTful API for Minecraft utilizing Springboot.",
    openGraph: {
        images: [
            {
                url: `${env.NEXT_PUBLIC_BASE_URL}/media/logo.png`,
                width: 128,
                height: 128,
            },
        ],
    },
    twitter: { card: "summary" },
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
                "min-h-screen antialiased scroll-smooth select-none",
                geistSans.variable,
                geistMono.variable
            )}
            style={{
                background:
                    "linear-gradient(to top, var(--alternative-background), var(--background))",
            }}
        >
            <ThemeProvider attribute="class" defaultTheme="dark" enableSystem>
                {children}
            </ThemeProvider>
        </body>
    </html>
);
export default RootLayout;
