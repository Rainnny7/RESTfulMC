import { cn } from "@/lib/utils";
import Link from "next/link";
import { ComponentProps, ReactNode } from "react";

type SimpleLinkProps = {
    /**
     * The optional class to append to the link.
     */
    className?: string | undefined;

    /**
     * The href to this link.
     */
    href: string;

    /**
     * The children to render within the link.
     */
    children: ReactNode;

    /**
     * Additional props to spread onto the underlying Link component.
     */
    props?: ComponentProps<typeof Link>;
};

const SimpleLink = ({ className, href, children, props }: SimpleLinkProps) => (
    <Link
        className={cn(
            "hover:opacity-75 transition-opacity transform-gpu",
            className
        )}
        href={href}
        draggable={false}
        {...props}
    >
        {children}
    </Link>
);
export default SimpleLink;
