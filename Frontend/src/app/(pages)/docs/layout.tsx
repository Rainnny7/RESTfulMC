import { ReactElement, ReactNode } from "react";
import Sidebar from "@/components/docs/sidebar";

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
    <section className="h-screen pt-64 flex justify-center">
        <div className="flex gap-10">
            <Sidebar />
            {children}
        </div>
    </section>
);
export default DocumentationLayout;
