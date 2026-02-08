"use client";

import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { env } from "@/lib/env";
import { cn } from "@/lib/utils";
import {
    ActivityIcon,
    BookIcon,
    FlameIcon,
    LucideIcon,
    PartyPopperIcon,
} from "lucide-react";
import { usePathname } from "next/navigation";
import {
    ReactElement,
    useEffect,
    useLayoutEffect,
    useRef,
    useState,
} from "react";
import {
    getMojangServerStatus,
    MojangServerStatus,
    MojangServerStatusResponse,
} from "restfulmc-lib";

type NavbarLink = {
    icon: LucideIcon;
    tooltip: string;
    label: string;
    href: string;
    getBadgeCount?: () => Promise<number>;
};

const links: NavbarLink[] = [
    {
        icon: FlameIcon,
        tooltip: "View all capes",
        label: "Capes",
        href: "/capes",
    },
    {
        icon: ActivityIcon,
        tooltip: "View the status of Microsoft and Mojang services",
        label: "Status",
        href: "/status",
        getBadgeCount: async () => {
            try {
                const status: MojangServerStatusResponse =
                    await getMojangServerStatus();
                return status.servers.filter(
                    (server) => server.status !== MojangServerStatus.ONLINE
                ).length;
            } catch {
                return 0;
            }
        },
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
    const [badgeCounts, setBadgeCounts] = useState<Record<string, number>>({});

    useEffect(() => {
        (async () => {
            const counts: Record<string, number> = {};
            for (const link of links) {
                if (link.getBadgeCount) {
                    try {
                        counts[link.label] = await link.getBadgeCount();
                    } catch {
                        counts[link.label] = 0;
                    }
                }
            }
            setBadgeCounts(counts);
        })();
    }, []);

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
                const badgeCount: number = link.getBadgeCount
                    ? (badgeCounts[link.label] ?? 0)
                    : 0;
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
                                    {badgeCount > 0 && (
                                        <Badge
                                            className="mr-0.5"
                                            variant="destructive"
                                        >
                                            {badgeCount}
                                        </Badge>
                                    )}
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
