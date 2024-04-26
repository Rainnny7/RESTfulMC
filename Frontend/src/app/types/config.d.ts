/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
    navbarLinks: NavbarLink;

    /**
     * Featured items for the landing page.
     */
    featuredItems: FeaturedItemProps[];

    /**
     * Links to display on the footer.
     */
    footerLinks: FooterLink;
}

/**
 * The links for the navbar.
 */
type NavbarLinks = {
    [name: string]: string;
};

/**
 * Links for the footer.
 */
type FooterLinks = {
    [header: string]: FooterLink;
};

/**
 * A link on the footer.
 */
type FooterLink = {
    [name: string]: string;
};

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
