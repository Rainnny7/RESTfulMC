"use client";

import AppLogo from "@/components/app-logo";
import LookupForm from "@/components/lookup-form";
import Links from "@/components/navbar/links";
import Socials from "@/components/navbar/socials";
import SimpleLink from "@/components/simple-link";
import { usePathname } from "next/navigation";
import { ReactElement, useState } from "react";

const Navbar = (): ReactElement => {
    const path: string = usePathname();
    const [lookupError, setLookupError] = useState<string | undefined>(
        undefined
    );
    return (
        <nav className="fixed inset-x-0 top-3.5 mx-auto max-w-7xl px-4 py-1 flex justify-between items-center bg-muted/20 backdrop-blur-sm rounded-xl z-50">
            {/* Left - Branding & Links */}
            <div className="flex gap-5 items-center">
                <SimpleLink className="flex gap-2.5 items-center" href="/">
                    <AppLogo />
                    <span className="text-lg font-bold">RESTfulMC</span>
                </SimpleLink>
                <Links />
            </div>

            {/* Right - Search & Socials */}
            <div className="flex gap-2.5 items-center">
                {path !== "/" && (
                    <LookupForm
                        className="min-w-50"
                        placeholder="Player / Server Lookup"
                        error={lookupError}
                        setError={setLookupError}
                    />
                )}
                <Socials />
            </div>
        </nav>
    );
};
export default Navbar;
