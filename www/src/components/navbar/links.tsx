"use client";

import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import { Button } from "@/components/ui/button";
import { env } from "@/lib/env";
import { cn } from "@/lib/utils";
import {
    BadgeIcon,
    BookIcon,
    LucideIcon,
    PartyPopperIcon,
    ServerIcon,
} from "lucide-react";
import { usePathname } from "next/navigation";
import { ReactElement, useLayoutEffect, useRef, useState } from "react";

type NavbarLink = {
    icon: LucideIcon;
    tooltip: string;
    label: string;
    href: string;
};

const links: NavbarLink[] = [
    {
        icon: ServerIcon,
        tooltip: "View featured servers",
        label: "Servers",
        href: "/servers",
    },
    {
        icon: PartyPopperIcon,
        tooltip: "View all capes",
        label: "Capes",
        href: "/capes",
    },
    {
        icon: BookIcon,
        tooltip: "View the documentation",
        label: "Docs",
        href: `${env.NEXT_PUBLIC_API_URL}/docs`,
    },
];

const Links = (): ReactElement => {
    const path: string = usePathname();
    const tabRefs = useRef<(HTMLDivElement | null)[]>([]);
    const [underlineStyle, setUnderlineStyle] = useState({ width: 0, left: 0 });
    const [hasMeasured, setHasMeasured] = useState(false);

    // Find the active link index
    const activeLinkIndex: number = links.findIndex(
        (link: NavbarLink) => path === link.href
    );

    // Update underline position when active tab changes.
    // useLayoutEffect runs before paint so we avoid a flash of wrong position.
    // Don't show indicator until measured to prevent visible width expansion on initial load.
    useLayoutEffect(() => {
        if (activeLinkIndex < 0) {
            setHasMeasured(false);
            return;
        }

        const activeTab = tabRefs.current[activeLinkIndex];
        if (!activeTab) return;

        setUnderlineStyle({
            width: activeTab.offsetWidth,
            left: activeTab.offsetLeft,
        });
        setHasMeasured(true);
    }, [activeLinkIndex, path]);

    return (
        <div className="relative flex gap-0.5 items-center">
            {links.map((link: NavbarLink, index: number) => {
                const active: boolean = path === link.href;
                const LinkIcon: LucideIcon = link.icon;
                return (
                    <SimpleTooltip key={link.label} content={link.tooltip}>
                        <div
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
                                    size="sm"
                                >
                                    <LinkIcon
                                        className={cn(
                                            "transition-colors duration-300 ease-in-out transform-gpu",
                                            active && "text-primary"
                                        )}
                                    />
                                    <span className="hidden md:block">
                                        {link.label}
                                    </span>
                                </Button>
                            </SimpleLink>
                        </div>
                    </SimpleTooltip>
                );
            })}

            {/* Active tab indicator - always mounted so it never animates from (0,0) on remount */}
            <div
                className="absolute top-0 h-7 bg-primary/5 rounded-sm border border-primary/30 transition-all duration-300 ease-in-out transform-gpu"
                style={{
                    width: `${underlineStyle.width}px`,
                    left: `${underlineStyle.left}px`,
                    opacity: activeLinkIndex >= 0 && hasMeasured ? 1 : 0,
                    pointerEvents: activeLinkIndex >= 0 ? "auto" : "none",
                }}
            >
                <div className="absolute left-0 -bottom-1.5 w-full h-0.5 bg-primary rounded-sm" />
            </div>
        </div>
    );
};

export default Links;
