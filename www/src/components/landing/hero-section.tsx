import { cn } from "@/lib/utils";
import { ReactElement } from "react";

const HeroSection = (): ReactElement => (
    <div className="mask-[linear-gradient(to_bottom,black_25%,transparent)]">
        <div
            className={cn(
                "relative min-h-172 flex flex-col justify-center items-center",
                "bg-cover bg-center bg-no-repeat",
                "mask-[radial-gradient(ellipse_at_center,black_30%,transparent_100%)]"
            )}
            style={{
                backgroundImage: `url(/media/landing.webp)`,
            }}
        >
            {/* Dark Overlay */}
            <div className="absolute inset-0 bg-black/15" />

            {/* Content */}
            <section className="flex flex-col gap-2 items-center z-10">
                bob ross
            </section>
        </div>
    </div>
);
export default HeroSection;
