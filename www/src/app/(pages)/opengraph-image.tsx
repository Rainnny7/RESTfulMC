import { env } from "@/lib/env";
import {
    OG_IMAGE_HEIGHT,
    OG_IMAGE_WIDTH,
    OgImageTemplate,
} from "@/lib/og-image";
import { ImageResponse } from "next/og";

export const alt =
    "RESTfulMC – A powerful RESTful API for Minecraft utilizing Spring Boot.";
export const size = { width: OG_IMAGE_WIDTH, height: OG_IMAGE_HEIGHT };
export const contentType = "image/png";

const PagesOgImage = () => {
    return new ImageResponse(
        <OgImageTemplate
            title="A powerful RESTful API for Minecraft"
            description="Simple, useful REST API for Minecraft utilizing Spring Boot."
            imageUrl={`${env.NEXT_PUBLIC_BASE_URL}/media/landing.webp`}
        />,
        { width: OG_IMAGE_WIDTH, height: OG_IMAGE_HEIGHT }
    );
};
export default PagesOgImage;
