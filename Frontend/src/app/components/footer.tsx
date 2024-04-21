import Image from "next/image";
import Link from "next/link";
import { ReactElement } from "react";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import { FooterLinks } from "@/types/config";

/**
 * The footer for the site.
 *
 * @returns the footer jsx
 */
const Footer = (): ReactElement => (
    <footer
        className={cn(
            `before:absolute before:top-0 before:left-0 before:w-full before:h-full before:bg-[url("/media/dark-wool-background.png")] before:bg-center before:bg-repeat`, // Background
            "relative inset-x-0 bottom-0 h-72 flex justify-center items-center", // Styling
            `after:absolute after:top-0 after:left-0 after:-translate-y-20 after:w-full after:h-20 after:bg-[url("/media/dark-wool-transition.png")] after:bg-center after:bg-repeat` // Top Border
        )}
    >
        <div className="xl:px-40 pb-14 md:pb-0 flex flex-col md:flex-row justify-around items-center z-50 w-full h-full transition-all transform-gpu">
            {/* Branding */}
            <div className="flex flex-col justify-center select-none pointer-events-none">
                {/* Logo & Site Name */}
                <div className="flex gap-7 items-center">
                    <Image
                        src="/media/logo.png"
                        alt="Site Logo"
                        width={56}
                        height={56}
                    />

                    <h1
                        className={cn(
                            "text-4xl sm:text-5xl md:text-4xl lg:text-5xl text-minecraft-green-3",
                            minecrafter.className
                        )}
                    >
                        {config.siteName}
                    </h1>
                </div>
            </div>

            {/* Links */}
            <div className="flex gap-16">
                {Object.keys(config.footerLinks).map(
                    (header: string, groupIndex: number): ReactElement => (
                        <div key={groupIndex} className="flex flex-col gap-2">
                            {/* Header */}
                            <h1
                                className={cn(
                                    "text-2xl xs:text-3xl text-minecraft-green-3 select-none pointer-events-none",
                                    minecrafter.className
                                )}
                            >
                                {header}
                            </h1>

                            {/* Links */}
                            <div className="flex flex-col gap-1">
                                {Object.entries(
                                    (config.footerLinks as FooterLinks)[header]
                                ).map(
                                    (
                                        [name, url]: [string, string],
                                        linkIndex: number
                                    ): ReactElement => (
                                        <Link
                                            key={linkIndex}
                                            className="font-semibold hover:opacity-85 transition-all transform-gpu"
                                            href={url}
                                        >
                                            {name}
                                        </Link>
                                    )
                                )}
                            </div>
                        </div>
                    )
                )}
            </div>

            {/* Disclaimer */}
            <p className="absolute inset-x-0 bottom-5 flex justify-center font-medium opacity-50">
                Not affiliated with Mojang or Microsoft.
            </p>
        </div>
    </footer>
);
export default Footer;
