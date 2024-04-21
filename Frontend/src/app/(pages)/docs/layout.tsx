import { ReactElement, ReactNode } from "react";
import Sidebar from "@/components/docs/sidebar";

/**
 * Force the layout to be static.
 */
export const dynamic = "force-static";

/**
 * The layout for the docs page.
 *
 * @param children the children of this layout
 * @returns the layout jsx
 */
const DocumentationLayout = ({
    children,
}: Readonly<{
    children: ReactNode;
}>): ReactElement => (
    <section className="min-h-screen py-28 flex justify-center">
        <div className="flex flex-wrap gap-32 divide-x-2">
            <Sidebar />
            {children}
        </div>
    </section>
);
export default DocumentationLayout;
