import { ReactElement } from "react";
import { cn } from "@/app/common/utils";

/**
 * The background hero component.
 *
 * @returns the background jsx
 */
const HeroBackground = (): ReactElement => (
    <div
        className={cn(
            "before:absolute before:left-0 before:top-0 before:w-full before:h-full before:bg-black/65", // Dark Overlay
            `absolute top-0 left-0 w-full h-full bg-[url("/media/background.jpg")] bg-cover bg-center -z-10`, // Background
            "after:absolute after:left-0 after:bottom-0 after:w-full after:h-1 after:bg-border" // Bottom Border
        )}
    />
);
export default HeroBackground;
