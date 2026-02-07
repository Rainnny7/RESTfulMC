"use client";

import AppLogo from "@/components/app-logo";
import Links from "@/components/navbar/links";
import NavbarLookupForm from "@/components/navbar/navbar-lookup-form";
import Socials from "@/components/navbar/socials";
import SimpleLink from "@/components/simple-link";
import { useScrolled } from "@/hooks/use-scrolled";
import { cn } from "@/lib/utils";
import { usePathname } from "next/navigation";
import { ReactElement } from "react";

const Navbar = (): ReactElement => {
    const path: string = usePathname();
    const { scrolled } = useScrolled();
    return (
        <nav
            className={cn(
                "fixed inset-x-2.5 sm:inset-x-5 top-3.5 mx-auto max-w-7xl px-4 py-2.5 flex justify-between items-center bg-muted/40 backdrop-blur-sm rounded-xl transition-all duration-300 transform-gpu z-50",
                scrolled && "top-0 rounded-t-none"
            )}
        >
            {/* Left */}
            <div className="flex gap-2 items-center">
                {/* Branding */}
                <SimpleLink className="flex gap-2.5 items-center" href="/">
                    <AppLogo />
                    <span className="hidden lg:block text-lg font-bold">
                        RESTfulMC
                    </span>
                </SimpleLink>

                {/* Links */}
                <Links />
            </div>

            {/* Right */}
            <div className="flex gap-5 items-center">
                {/* Search */}
                {path !== "/" && <NavbarLookupForm />}

                {/* Socials */}
                <div className="hidden lg:block">
                    <Socials />
                </div>
            </div>
        </nav>
    );
};
export default Navbar;
