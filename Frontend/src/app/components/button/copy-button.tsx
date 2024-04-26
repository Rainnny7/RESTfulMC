/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
