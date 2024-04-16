import FeaturedContent from "@/components/landing/featured-content";
import Hero from "@/components/landing/hero";

/**
 * The landing page.
 *
 * @returns the page jsx
 */
const LandingPage = (): JSX.Element => (
	<main className="px-3">
		<Hero />
		<FeaturedContent />
	</main>
);
export default LandingPage;
