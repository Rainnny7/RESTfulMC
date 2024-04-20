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
    <section className="h-screen flex flex-col justify-center items-center">
        <Sidebar />
        {children}
    </section>
);
export default DocumentationLayout;
