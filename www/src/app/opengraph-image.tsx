import { env } from "@/lib/env";
import {
    OG_IMAGE_HEIGHT,
    OG_IMAGE_WIDTH,
    OgImageTemplate,
} from "@/lib/og-image";
import { ImageResponse } from "next/og";

export const alt = "RESTfulMC – A simple, yet useful RESTful API for Minecraft";
export const size = { width: OG_IMAGE_WIDTH, height: OG_IMAGE_HEIGHT };
export const contentType = "image/png";

const RootOgImage = () => {
    return new ImageResponse(
        <OgImageTemplate
            title="RESTfulMC"
            description="A simple, yet useful RESTful API for Minecraft utilizing Spring Boot."
            siteName="RESTfulMC"
            imageUrl={`${env.NEXT_PUBLIC_BASE_URL}/media/landing.webp`}
        />,
        { width: OG_IMAGE_WIDTH, height: OG_IMAGE_HEIGHT }
    );
};
export default RootOgImage;
