import { env } from "@/lib/env";
import {
    OgImageTemplate,
    OG_IMAGE_HEIGHT,
    OG_IMAGE_WIDTH,
} from "@/lib/og-image";
import { ImageResponse } from "next/og";

export const alt =
    "RESTfulMC – A powerful RESTful API for Minecraft utilizing Spring Boot.";
export const size = { width: OG_IMAGE_WIDTH, height: OG_IMAGE_HEIGHT };
export const contentType = "image/png";

const PagesOgImage = () => {
    const baseUrl = env.NEXT_PUBLIC_BASE_URL ?? "http://localhost:3000";
    return new ImageResponse(
        (
            <OgImageTemplate
                title="A powerful RESTful API for Minecraft"
                description="Simple, useful REST API for Minecraft utilizing Spring Boot."
                siteName="RESTfulMC"
                imageUrl={`${baseUrl}/media/landing.webp`}
            />
        ),
        { width: OG_IMAGE_WIDTH, height: OG_IMAGE_HEIGHT }
    );
};

export default PagesOgImage;
