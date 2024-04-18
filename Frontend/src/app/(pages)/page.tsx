import Background from "@/components/landing/background";
import FeaturedContent from "@/components/landing/featured-content";
import Hero from "@/components/landing/hero";
import StatisticCounters from "@/components/landing/statistic-counters";
import { ReactElement } from "react";

/**
 * The landing page.
 *
 * @returns the page jsx
 */
const LandingPage = (): ReactElement => (
    <main className="flex flex-col gap-10">
        {/* Hero */}
        <div className="relative">
            <Background />
            <Hero />
        </div>

        {/* Content */}
        <div className="px-3">
            <FeaturedContent />
            <StatisticCounters />
        </div>
    </main>
);
export default LandingPage;
