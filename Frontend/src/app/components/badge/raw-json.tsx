import { ReactElement } from "react";
import { Badge } from "@/components/ui/badge";
import SimpleTooltip from "@/components/simple-tooltip";

/**
 * A badge for raw json.
 *
 * @returns the raw json badge jsx
 */
const RawJsonBadge = (): ReactElement => (
    <SimpleTooltip content="Click to view raw JSON">
        <Badge className="bg-minecraft-green-2 hover:bg-minecraft-green-2 hover:opacity-85 text-white font-semibold uppercase transition-all transform-gpu">
            Raw Json
        </Badge>
    </SimpleTooltip>
);
export default RawJsonBadge;
