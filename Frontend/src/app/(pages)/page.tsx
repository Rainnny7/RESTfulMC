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
    <main className="px-3">
        <Hero />
        <FeaturedContent />
        <StatisticCounters />
    </main>
);
export default LandingPage;
