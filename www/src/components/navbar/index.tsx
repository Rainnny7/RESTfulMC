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
                "fixed inset-x-5 top-3.5 mx-auto max-w-7xl px-4 py-1 flex justify-between items-center bg-muted/20 backdrop-blur-sm rounded-xl transition-all duration-300 transform-gpu z-50",
                scrolled && "top-0 rounded-t-none"
            )}
        >
            {/* Left - Branding & Links */}
            <div className="flex gap-2 items-center">
                <SimpleLink className="flex gap-2.5 items-center" href="/">
                    <AppLogo />
                    <span className="hidden xs:block text-lg font-bold">
                        RESTfulMC
                    </span>
                </SimpleLink>
                <Links />
            </div>

            {/* Right - Search & Socials */}
            <div className="flex gap-5 items-center">
                {path !== "/" && <NavbarLookupForm />}
                <div className="hidden sm:block">
                    <Socials />
                </div>
            </div>
        </nav>
    );
};
export default Navbar;
