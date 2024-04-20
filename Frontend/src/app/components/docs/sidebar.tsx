import { ReactElement } from "react";
import { Input } from "@/components/ui/input";

/**
 * The sidebar for the docs page.
 *
 * @returns the sidebar jsx
 */
const Sidebar = (): ReactElement => (
    <div className="w-60 h-80 px-3 py-5 flex justify-center bg-muted border border-zinc-700/70 rounded-lg">
        {/* Search */}
        <Input
            type="search"
            name="search"
            placeholder="Quick search..."
            disabled
        />
    </div>
);
export default Sidebar;
