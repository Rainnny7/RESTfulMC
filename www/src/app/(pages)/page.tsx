import HeroSection from "@/components/landing/hero-section";
import { Metadata } from "next";
import { ReactElement } from "react";

export const metadata: Metadata = {
    title: "A powerful RESTful API for Minecraft",
};

const LandingPage = (): ReactElement => (
    <main className="min-h-screen flex flex-col">
        <HeroSection />
        <p>under section</p>
    </main>
);
export default LandingPage;
