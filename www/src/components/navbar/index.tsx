import AppLogo from "@/components/app-logo";
import Links from "@/components/navbar/links";
import Socials from "@/components/navbar/socials";
import SimpleLink from "@/components/simple-link";
import { ReactElement } from "react";

const Navbar = (): ReactElement => (
    <nav className="fixed inset-x-0 top-3.5 mx-auto max-w-7xl px-4 py-1 flex justify-between items-center bg-muted/20 backdrop-blur-sm rounded-xl z-50">
        {/* Left - Branding & Links */}
        <div className="flex gap-5 items-center">
            <SimpleLink className="flex gap-2.5 items-center" href="/">
                <AppLogo />
                <span className="text-lg font-bold">RESTfulMC</span>
            </SimpleLink>
            <Links />
        </div>

        {/* Right - Socials */}
        <div className="flex gap-2.5 items-center">
            <Socials />
        </div>
    </nav>
);
export default Navbar;
