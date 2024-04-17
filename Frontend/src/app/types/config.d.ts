import { Metadata, Viewport } from "next";

/**
 * Options for configuration.
 */
interface Config {
    /**
     * The name of this site.
     */
    siteName: string;

    /**
     * The URL of this site.
     */
    siteUrl: string;

    /**
     * The endpoint of the API.
     */
    apiEndpoint: string;

    /**
     * The optional domain to track analytics on.
     */
    analyticsDomain: string | undefined;

    /**
     * The metadata of this site.
     */
    metadata: Metadata;

    /**
     * The viewport of this site.
     */
    viewport: Viewport;

    /**
     * Links to display on the navbar.
     * <p>
     * The key is the name of the
     * link, and the value is the URL.
     * </p>
     */
    navbarLinks: {
        [name: string]: string;
    };

    /**
     * Featured items for the landing page.
     */
    featuredItems: FeaturedItemProps[];
}

/**
 * Props for a featured item
 * on the landing page.
 */
type FeaturedItemProps = {
    /**
     * The name of this item.
     */
    name: string;

    /**
     * The description of this item.
     */
    description: string;

    /**
     * The image for this item.
     */
    image: string;

    /**
     * The href link for this item.
     */
    href: string;
};
