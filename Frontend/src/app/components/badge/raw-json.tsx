import { ReactElement } from "react";
import { Badge } from "@/components/ui/badge";

/**
 * A badge for raw json.
 *
 * @returns the raw json badge jsx
 */
const RawJsonBadge = (): ReactElement => (
    <Badge className="bg-minecraft-green-2 hover:bg-minecraft-green-2 hover:opacity-85 text-white font-semibold uppercase transition-all transform-gpu">
        Raw Json
    </Badge>
);
export default RawJsonBadge;
