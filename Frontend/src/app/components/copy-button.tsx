"use client";

import { ReactElement, ReactNode } from "react";
import copy from "clipboard-copy";

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
};

/**
 * A component that copies
 */
const CopyButton = ({ content, children }: CopyButtonProps): ReactElement => (
    <button onClick={async () => await copy(content)}>{children}</button>
);
export default CopyButton;
