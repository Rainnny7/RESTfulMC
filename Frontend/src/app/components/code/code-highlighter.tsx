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
import { ReactElement } from "react";
import SyntaxHighlighter from "react-syntax-highlighter";
import { atomOneDark } from "react-syntax-highlighter/dist/esm/styles/hljs";
import Image from "next/image";
import { capitalize } from "@/lib/string-utils";
import { cn } from "@/lib";

/**
 * Props for the code highlighter.
 */
type CodeHighlighterProps = {
    /**
     * The class name for the code block.
     */
    className?: string | undefined;

    /**
     * The language of the code, if any
     */
    language: string | undefined;

    /**
     * The code to highlight.
     */
    children: string;
};

/**
 * A code highlighter component.
 *
 * @param className the class name for the code block
 * @param language the language for syntax highlighting
 * @param children the children (code) to highlight
 * @return the highlighter jsx
 */
const CodeHighlighter = ({
    className,
    language,
    children,
}: CodeHighlighterProps): ReactElement => {
    // Return a basic code block if no language is provided
    if (!language) {
        return (
            <code
                className={cn(
                    "p-2 block bg-muted/70 break-all rounded-lg",
                    className
                )}
            >
                {children}
            </code>
        );
    }
    return (
        <div className="relative">
            <SyntaxHighlighter
                className={cn("!bg-muted/70 break-all rounded-lg", className)}
                language={language}
                style={atomOneDark}
                wrapLongLines
            >
                {children}
            </SyntaxHighlighter>

            {/* Language Icon */}
            <div className="absolute top-0 right-0 px-2 py-0.5 flex gap-2 items-center text-white/75 bg-zinc-700/50 rounded select-none pointer-events-none">
                <Image
                    src={`https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/${language}/${language}-original.svg`}
                    alt={`${language} Language Icon`}
                    width={20}
                    height={20}
                />
                {capitalize(language)}
            </div>
        </div>
    );
};
export default CodeHighlighter;
