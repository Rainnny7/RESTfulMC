import { cn } from "@/lib/utils";
import { ExternalLinkIcon } from "lucide-react";
import Link from "next/link";
import { ComponentProps, ReactNode } from "react";

type SimpleLinkProps = ComponentProps<typeof Link> & {
    href: string;
    externalIcon?: boolean;
    children: ReactNode;
};

const SimpleLink = ({
    className,
    href,
    externalIcon = false,
    children,
    ...rest
}: SimpleLinkProps) => {
    const isExternal: boolean = href.startsWith("http");
    return (
        <Link
            className={cn(
                "flex gap-1 items-center hover:opacity-75 transition-opacity transform-gpu",
                className
            )}
            href={href}
            target={isExternal ? "_blank" : undefined}
            rel={isExternal ? "noopener noreferrer" : undefined}
            draggable={false}
            {...rest}
        >
            {children}
            {isExternal && externalIcon && (
                <ExternalLinkIcon className="ml-0.5 size-3.5" />
            )}
        </Link>
    );
};
export default SimpleLink;
