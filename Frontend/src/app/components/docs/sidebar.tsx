import { ReactElement } from "react";
import { Input } from "@/components/ui/input";
import { getDocsContent } from "@/lib/mdxUtils";

/**
 * The sidebar for the docs page.
 *
 * @returns the sidebar jsx
 */
const Sidebar = (): ReactElement => (
    <div className="hidden h-full px-3 py-5 xl:flex flex-col items-center">
        <div className="fixed w-56 flex flex-col gap-2.5">
            {/* Search */}
            <Input
                type="search"
                name="search"
                placeholder="Quick search..."
                disabled
            />

            {/* Links */}
            <div className="flex flex-col gap-1">
                {getDocsContent().map(
                    (
                        content: DocsContentMetadata,
                        index: number
                    ): ReactElement => (
                        <div key={index}>{content.title}</div>
                    )
                )}
            </div>
        </div>
    </div>
);
export default Sidebar;
