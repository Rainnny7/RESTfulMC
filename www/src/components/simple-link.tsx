import { cn } from "@/lib/utils";
import Link from "next/link";
import { ComponentProps, ReactNode } from "react";

type SimpleLinkProps = ComponentProps<typeof Link> & {
    href: string;
    children: ReactNode;
};

const SimpleLink = ({
    className,
    href,
    children,
    ...rest
}: SimpleLinkProps) => {
    const isExternal = href.startsWith("http");
    return (
        <Link
            className={cn(
                "hover:opacity-75 transition-opacity transform-gpu",
                className
            )}
            href={href}
            target={isExternal ? "_blank" : undefined}
            rel={isExternal ? "noopener noreferrer" : undefined}
            draggable={false}
            {...rest}
        >
            {children}
        </Link>
    );
};
export default SimpleLink;
