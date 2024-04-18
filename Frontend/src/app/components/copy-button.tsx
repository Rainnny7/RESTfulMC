"use client";

import copy from "clipboard-copy";
import { ReactElement, ReactNode } from "react";
import { toast } from "sonner";

/**
 * Props for the copy button.
 */
type CopyButtonProps = {
    /**
     * The content to copy when
     * this component is clicked.
     */
    content: string;

    /**
     * The children to render in this button.
     */
    children: ReactNode;

    /**
     * Should a toast be shown
     * when this button is clicked?
     */
    showToast: boolean;
};

/**
 * A component that copies
 * content to the clipboard.
 *
 * @returns the component
 */
const CopyButton = ({
    content,
    children,
    showToast,
}: CopyButtonProps): ReactElement => {
    const handleClick = async (): Promise<void> => {
        await copy(content); // Copy to the clipboard

        // Show a toast when copied
        if (showToast) {
            toast("Copied", {
                description: `Copied "${content}" to your clipboard`,
            });
        }
    };
    return <button onClick={handleClick}>{children}</button>;
};
export default CopyButton;
