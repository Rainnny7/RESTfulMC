import Footer from "@/components/footer";
import Navbar from "@/components/navbar";
import { env } from "@/lib/env";
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
        template: "%s • RESTfulMC",
    },
    description:
        "🌐 A simple, yet useful RESTful API for Minecraft utilizing Springboot.",
    openGraph: {
        images: [
            {
                url: `${env.NEXT_PUBLIC_BASE_URL}/media/logo/logo.webp`,
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
    <html lang="en">
        <body
            className={cn(
                "antialiased scroll-smooth select-none",
                geistSans.variable,
                geistMono.variable
            )}
        >
            <AppProvider>
                <div
                    style={{
                        background:
                            "linear-gradient(to top, var(--color-alternative-background), var(--color-background))",
                    }}
                >
                    <Navbar />
                    {children}
                    <Footer />
                </div>
            </AppProvider>
        </body>
    </html>
);
export default RootLayout;
