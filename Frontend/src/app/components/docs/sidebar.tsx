import { ReactElement } from "react";

/**
 * The sidebar for the docs page.
 *
 * @returns the sidebar jsx
 */
const Sidebar = (): ReactElement => (
    <div className="absolute left-20 h-72 p-5 bg-muted rounded-xl">
        <h1 className="font-semibold uppercase">SIDEBAR</h1>
    </div>
);
export default Sidebar;
