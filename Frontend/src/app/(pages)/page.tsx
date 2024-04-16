import FeaturedContent from "@/components/landing/featured-content";
import Hero from "@/components/landing/hero";

/**
 * The landing page of the site.
 *
 * @returns the landing page jsx
 */
const LandingPage = (): JSX.Element => (
	<main>
		<Hero />
		<FeaturedContent />
	</main>
);
export default LandingPage;
