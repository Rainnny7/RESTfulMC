import { cn } from "@/lib/utils";
import { ReactElement } from "react";

const HeroSection = (): ReactElement => (
    <div className="relative min-h-172 flex flex-col justify-center items-center overflow-hidden">
        {/* Background */}
        <div className="absolute inset-0 mask-[linear-gradient(to_bottom,black_25%,transparent)]">
            <div
                className={cn(
                    "h-full w-full bg-cover bg-center bg-no-repeat",
                    "mask-[radial-gradient(ellipse_at_center,black_30%,transparent_100%)]"
                )}
                style={{
                    backgroundImage: `url(/media/landing.webp)`,
                }}
            />

            {/* Darker at bottom to blend with background */}
            <div className="absolute inset-0 bg-linear-to-b from-transparent via-transparent to-background" />
        </div>

        {/* Content */}
        <section className="relative flex flex-col gap-2 items-center z-10">
            <p>sdfsdfsdfsds</p>
        </section>
    </div>
);
export default HeroSection;
