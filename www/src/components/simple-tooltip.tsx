import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from "@/components/ui/tooltip";
import { type SIDE_OPTIONS } from "@radix-ui/react-popper";
import type { ReactElement, ReactNode } from "react";

/**
 * The props for a simple tooltip.
 */
type SimpleTooltipProps = {
    /**
     * The content to display in the tooltip.
     */
    content: string | ReactElement | undefined;

    /**
     * The side to display the tooltip on.
     */
    side?: (typeof SIDE_OPTIONS)[number];

    /**
     * The children to render in this tooltip.
     */
    children: ReactNode;
};

/**
 * A simple tooltip, this is wrapping the
 * shadcn tooltip to make it easier to use.
 *
 * @return the tooltip jsx
 */
const SimpleTooltip = ({
    content,
    side,
    children,
}: SimpleTooltipProps): ReactElement =>
    content ? (
        <Tooltip>
            <TooltipTrigger asChild>{children}</TooltipTrigger>
            <TooltipContent side={side}>{content}</TooltipContent>
        </Tooltip>
    ) : (
        <>{children}</>
    );
export default SimpleTooltip;
