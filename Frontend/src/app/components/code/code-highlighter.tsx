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
