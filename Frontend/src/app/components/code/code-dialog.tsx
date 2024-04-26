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
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import CodeHighlighter from "@/components/code/code-highlighter";
import { ReactElement } from "react";

/**
 * Props for the code dialog.
 */
type CodeDialogProps = {
    /**
     * The title of this dialog.
     */
    title: string;

    /**
     * The description of this dialog.
     */
    description: string;

    /**
     * The language of the code, if any
     */
    language: string | undefined;

    /**
     * The trigger to open this dialog.
     */
    trigger: ReactElement;

    /**
     * The code in the dialog.
     */
    children: string;
};

/**
 * A dialog to display code.
 *
 * @param title the title of this dialog
 * @param description the description of this dialog
 * @param trigger the trigger to open this dialog
 * @param language the language of the code
 * @param children the children (code) to display
 * @return the dialog jsx
 */
const CodeDialog = ({
    title,
    description,
    language,
    trigger,
    children,
}: CodeDialogProps) => (
    <Dialog>
        <DialogTrigger>{trigger}</DialogTrigger>
        <DialogContent className="max-w-none w-[90vw] lg:w-[85vw] xl:w-[80vw] 2xl:w-[70vw] max-h-[90vh]">
            {/* Header */}
            <DialogHeader>
                <DialogTitle>{title}</DialogTitle>
                <DialogDescription>{description}</DialogDescription>
            </DialogHeader>

            {/* Code */}
            <CodeHighlighter className="max-h-[70vh]" language={language}>
                {children}
            </CodeHighlighter>
        </DialogContent>
    </Dialog>
);
export default CodeDialog;
