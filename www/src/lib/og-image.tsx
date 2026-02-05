import type { ReactElement } from "react";

/**
 * Open Graph image template and constants for Next.js `opengraph-image.tsx` routes.
 * Use {@link OgImageTemplate} inside `ImageResponse` for consistent, per-route OG images.
 *
 * @module
 */

/**
 * Standard Open Graph image dimensions (1200×630).
 * Use these when creating `ImageResponse` so embeds look correct on social platforms.
 */
export const OG_IMAGE_WIDTH = 1200;
export const OG_IMAGE_HEIGHT = 630;

/**
 * Props for the OG image template.
 *
 * @property title - Main heading shown on the image (required).
 * @property description - Optional subtitle or tagline (rendered in bold).
 * @property imageUrl - Optional absolute URL for a background/accent image (e.g. `${baseUrl}/media/landing.webp`). Shown dimmed on the right; omit for pattern-only background. Note: Site name is always "RESTfulMC" and cannot be customized.
 */
export type OgImageProps = {
    title: string;
    description?: string;
    imageUrl?: string;
};

/**
 * Reusable Open Graph image template for Next.js `opengraph-image.tsx` routes.
 *
 * Renders a 1200×630 layout with:
 * - Dark gradient background and subtle geometric pattern
 * - Optional dimmed accent image on the right (when `imageUrl` is set, opacity 0.4)
 * - Site name "RESTfulMC" (always shown), title, and optional bold description
 * - Bottom gradient accent line
 */
export const OgImageTemplate = ({
    title,
    description,
    imageUrl,
}: OgImageProps): ReactElement => {
    return (
        <div
            style={{
                width: "100%",
                height: "100%",
                display: "flex",
                flexDirection: "column",
                background: "linear-gradient(145deg, #1a1b2e 0%, #16213e 40%, #0f3460 100%)",
                fontFamily: "system-ui, sans-serif",
                position: "relative",
                overflow: "hidden",
            }}
        >
            {/* Geometric pattern overlay */}
            <div
                style={{
                    position: "absolute",
                    inset: 0,
                    background: `
            linear-gradient(30deg, rgba(100, 104, 240, 0.08) 12%, transparent 12.5%, transparent 87%, rgba(100, 104, 240, 0.08) 87.5%),
            linear-gradient(150deg, rgba(100, 104, 240, 0.08) 12%, transparent 12.5%, transparent 87%, rgba(100, 104, 240, 0.08) 87.5%),
            linear-gradient(30deg, rgba(100, 104, 240, 0.08) 12%, transparent 12.5%, transparent 87%, rgba(100, 104, 240, 0.08) 87.5%),
            linear-gradient(150deg, rgba(100, 104, 240, 0.08) 12%, transparent 12.5%, transparent 87%, rgba(100, 104, 240, 0.08) 87.5%),
            linear-gradient(60deg, rgba(100, 104, 240, 0.04) 25%, transparent 25.5%, transparent 75%, rgba(100, 104, 240, 0.04) 75.5%)
          `,
                    backgroundSize: "80px 140px",
                    backgroundPosition: "0 0, 0 0, 40px 70px, 40px 70px, 0 0",
                }}
            />
            {/* Optional background image - dimmed */}
            {imageUrl ? (
                <div
                    style={{
                        position: "absolute",
                        inset: 0,
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "flex-end",
                        zIndex: 1,
                    }}
                >
                    <img
                        src={imageUrl}
                        alt=""
                        width={600}
                        height={630}
                        style={{
                            objectFit: "cover",
                            opacity: 0.4,
                            height: "100%",
                        }}
                    />
                </div>
            ) : null}
            {/* Content */}
            <div
                style={{
                    position: "relative",
                    display: "flex",
                    flex: 1,
                    flexDirection: "column",
                    justifyContent: "center",
                    padding: 64,
                    maxWidth: imageUrl ? 700 : 1100,
                    zIndex: 2,
                }}
            >
                <div
                    style={{
                        fontSize: 18,
                        color: "rgba(255,255,255,0.6)",
                        letterSpacing: "0.15em",
                        textTransform: "uppercase",
                        marginBottom: 12,
                    }}
                >
                    RESTfulMC
                </div>
                <h1
                    style={{
                        fontSize: 56,
                        fontWeight: 700,
                        color: "white",
                        lineHeight: 1.15,
                        margin: 0,
                        letterSpacing: "-0.02em",
                    }}
                >
                    {title}
                </h1>
                {description ? (
                    <p
                        style={{
                            fontSize: 24,
                            fontWeight: 700,
                            color: "rgba(255,255,255,0.85)",
                            lineHeight: 1.4,
                            marginTop: 20,
                            marginBottom: 0,
                        }}
                    >
                        {description}
                    </p>
                ) : null}
            </div>
            {/* Bottom accent line */}
            <div
                style={{
                    position: "absolute",
                    bottom: 0,
                    left: 0,
                    right: 0,
                    height: 4,
                    background: "linear-gradient(90deg, #6468f0 0%, #8b5cf6 100%)",
                }}
            />
        </div>
    );
};
