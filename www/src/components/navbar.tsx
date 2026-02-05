"use client";

import AppLogo from "@/components/app-logo";
import SimpleLink from "@/components/simple-link";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import {
    BadgeIcon,
    BookIcon,
    HomeIcon,
    LucideIcon,
    PartyPopperIcon,
    ServerIcon,
} from "lucide-react";
import { usePathname } from "next/navigation";
import { ReactElement, useEffect, useRef, useState } from "react";

type NavbarLink = {
    icon: LucideIcon;
    label: string;
    href: string;
};

const links: NavbarLink[] = [
    {
        icon: HomeIcon,
        label: "Home",
        href: "/",
    },
    {
        icon: ServerIcon,
        label: "Servers",
        href: "/servers",
    },
    {
        icon: PartyPopperIcon,
        label: "Capes",
        href: "/capes",
    },
    {
        icon: BadgeIcon,
        label: "Badges",
        href: "/badges",
    },
    {
        icon: BookIcon,
        label: "Docs",
        href: "/docs",
    },
];

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

        <div>sdfsd</div>
    </nav>
);

const Links = (): ReactElement => {
    const path: string = usePathname();
    const tabRefs = useRef<(HTMLDivElement | null)[]>([]);
    const [underlineStyle, setUnderlineStyle] = useState({ width: 0, left: 0 });

    // Find the active link index
    const activeLinkIndex: number = links.findIndex(
        (link: NavbarLink) => path === link.href
    );

    // Update underline position when active tab changes
    useEffect(() => {
        if (activeLinkIndex < 0) return;

        const activeTab = tabRefs.current[activeLinkIndex];
        if (!activeTab) return;

        // Use offsets relative to the container so the underline
        // always lines up with the active tab.
        setUnderlineStyle({
            width: activeTab.offsetWidth,
            left: activeTab.offsetLeft,
        });
    }, [activeLinkIndex, path]);

    return (
        <div className="relative flex gap-0.5 items-center">
            {links.map((link: NavbarLink, index: number) => {
                const active: boolean = path === link.href;
                const LinkIcon: LucideIcon = link.icon;
                return (
                    <div
                        key={link.label}
                        ref={(element) => {
                            tabRefs.current[index] = element;
                        }}
                    >
                        <SimpleLink href={link.href}>
                            <Button
                                className={cn(
                                    "relative text-muted-foreground transition-all duration-300 ease-in-out transform-gpu",
                                    active && "text-primary-foreground"
                                )}
                                variant="ghost"
                                size="xs"
                            >
                                <LinkIcon
                                    className={cn(active && "text-primary")}
                                />
                                <span>{link.label}</span>
                            </Button>
                        </SimpleLink>
                    </div>
                );
            })}

            {/* Active tab indicator */}
            {activeLinkIndex >= 0 && (
                <div
                    className="absolute top-0 h-6 bg-primary/5 rounded-sm border border-primary/15 transition-all duration-300 ease-in-out transform-gpu"
                    style={{
                        width: `${underlineStyle.width}px`,
                        left: `${underlineStyle.left}px`,
                    }}
                >
                    <div className="absolute -bottom-1.5 left-0 w-full h-0.5 bg-primary rounded-sm" />
                </div>
            )}
        </div>
    );
};

export default Navbar;
