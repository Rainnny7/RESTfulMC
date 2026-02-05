import { cn } from "@/lib/utils";
import Image from "next/image";

type AppLogoProps = {
    /**
     * The optional class to append to the logo.
     */
    className?: string | undefined;

    /**
     * The rendered size of the logo in pixels.
     * Applies to both width and height.
     */
    size?: number;
};

const AppLogo = ({ className, size = 28 }: AppLogoProps) => (
    <Image
        className={cn(className)}
        src="/media/logo.webp"
        alt="RESTfulMC Logo"
        width={size}
        height={size}
        unoptimized
        priority
        draggable={false}
    />
);
export default AppLogo;
