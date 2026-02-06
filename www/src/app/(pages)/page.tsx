import HeroSection from "@/components/landing/hero-section";
import PlayerExamples from "@/components/landing/player-examples";
import ServerExamples from "@/components/landing/server-examples";
import { Metadata } from "next";
import { ReactElement } from "react";

export const metadata: Metadata = {
    title: "A powerful RESTful API for Minecraft",
};

const LandingPage = (): ReactElement => (
    <main className="min-h-screen flex flex-col">
        <HeroSection />
        <div className="-mt-56 px-5 flex flex-col gap-5 items-center justify-center">
            <PlayerExamples />
            <ServerExamples />
        </div>
    </main>
);
export default LandingPage;
