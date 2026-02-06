import { cn } from "@/lib/utils";
import { ReactElement, ReactNode } from "react";

type PageHeaderProps = {
    className?: string | undefined;
    contentClassName?: string | undefined;
    backgroundImage: string;
    children: ReactNode;
};

const PageHeader = ({
    className,
    contentClassName,
    backgroundImage,
    children,
}: PageHeaderProps): ReactElement => (
    <div
        className={cn(
            "relative min-h-172 flex flex-col justify-center items-center overflow-hidden",
            className
        )}
    >
        {/* Background */}
        <div className="absolute inset-0 mask-[linear-gradient(to_bottom,black_25%,transparent)]">
            <div
                className={cn(
                    "h-full w-full bg-cover bg-center bg-no-repeat",
                    "mask-[radial-gradient(ellipse_at_center,black_30%,transparent_100%)]"
                )}
                style={{
                    backgroundImage: `url(${backgroundImage})`,
                }}
            />

            {/* Darker at bottom to blend with background */}
            <div className="absolute inset-0 bg-linear-to-b from-transparent via-transparent to-background" />
        </div>

        {/* Content */}
        <section
            className={cn(
                "relative px-5 w-full flex flex-col gap-2 items-center z-10",
                contentClassName
            )}
        >
            {children}
        </section>
    </div>
);
export default PageHeader;
