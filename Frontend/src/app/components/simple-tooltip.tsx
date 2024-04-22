import { ReactElement, ReactNode } from "react";
import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from "@/components/ui/tooltip";

/**
 * The props for a simple tooltip.
 */
type SimpleTooltipProps = {
    /**
     * The content to display in the tooltip.
     */
    content: string | ReactNode;

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
    children,
}: SimpleTooltipProps): ReactElement => (
    <Tooltip>
        <TooltipTrigger asChild>{children}</TooltipTrigger>
        <TooltipContent>{content}</TooltipContent>
    </Tooltip>
);

export default SimpleTooltip;
