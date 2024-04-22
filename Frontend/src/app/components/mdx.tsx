import { ReactElement, ReactNode } from "react";
import { MDXRemote } from "remote-mdx/rsc";
import { cn } from "@/lib/utils";
import remarkGfm from "remark-gfm";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import SyntaxHighlighter from "react-syntax-highlighter";
import {
    atomOneDark,
    nord,
    ocean,
    solarizedDark,
    stackoverflowDark,
} from "react-syntax-highlighter/dist/esm/styles/hljs";
import { capitalize } from "@/lib/stringUtils";

/**
 * The MDX components to style.
 */
const components: any = {
    h1: ({ children }: { children: ReactNode }): ReactElement => (
        <Heading size={1} className="text-4xl">
            {children}
        </Heading>
    ),
    h2: ({ children }: { children: ReactNode }): ReactElement => (
        <Heading size={2} className="text-3xl">
            {children}
        </Heading>
    ),
    h3: ({ children }: { children: ReactNode }): ReactElement => (
        <Heading size={3} className="text-2xl">
            {children}
        </Heading>
    ),
    h4: ({ children }: { children: ReactNode }): ReactElement => (
        <Heading size={4} className="text-xl">
            {children}
        </Heading>
    ),
    h5: ({ children }: { children: ReactNode }): ReactElement => (
        <Heading size={5} className="text-lg">
            {children}
        </Heading>
    ),
    h6: ({ children }: { children: ReactNode }): ReactElement => (
        <Heading size={5} className="text-md">
            {children}
        </Heading>
    ),
    a: ({
        href,
        children,
    }: {
        href: string;
        children: ReactNode;
    }): ReactElement => (
        <a
            className="text-minecraft-green-4 cursor-pointer hover:opacity-85 transition-all transform-gpu"
            href={href}
        >
            {children}
        </a>
    ),
    p: ({ children }: { children: ReactNode }): ReactElement => (
        <p className="leading-4 text-zinc-300/80">{children}</p>
    ),
    code: ({
        className,
        children,
    }: {
        className: string;
        children: any;
    }): ReactElement => {
        const language: string | undefined = className?.replace(
            "language-",
            ""
        ); // The language of the code, if any
        return language ? (
            <div className="relative max-w-5xl">
                <SyntaxHighlighter
                    className="!bg-muted/70 break-all rounded-lg"
                    language={language}
                    style={atomOneDark}
                    wrapLongLines
                >
                    {children}
                </SyntaxHighlighter>

                {/* Language Icon */}
                <div className="absolute top-0 right-0 px-2 py-0.5 text-white/75 bg-zinc-700/50 rounded select-none pointer-events-none">
                    {capitalize(language)}
                </div>
            </div>
        ) : (
            <code className="p-2 max-w-5xl block bg-muted/70 break-all rounded-lg">
                {children}
            </code>
        );
    },
    ul: ({ children }: { children: ReactNode }): ReactElement => (
        <ul className="px-3 list-disc list-inside">{children}</ul>
    ),

    // Tables
    table: ({ children }: { children: any }) => (
        <Table>
            <TableHeader>
                <TableRow>
                    {children?.[0].props?.children?.props?.children}
                </TableRow>
            </TableHeader>
            <TableBody>{children?.[1].props?.children}</TableBody>
        </Table>
    ),
    th: ({ children }: { children: ReactNode }) => (
        <TableHead>{children}</TableHead>
    ),
    td: ({ children }: { children: ReactNode }) => (
        <TableCell>{children}</TableCell>
    ),
};

/**
 * The custom render for MDX.
 *
 * @param props the props for the MDX
 * @return the custom mdx
 */
export const CustomMDX = (props: any): ReactElement => (
    <MDXRemote
        {...props}
        components={{
            ...components,
            ...(props.components || {}),
        }}
        options={{
            mdxOptions: {
                remarkPlugins: [remarkGfm],
            },
        }}
    />
);

/**
 * A heading component.
 *
 * @param className the class name of the heading
 * @param size the size of the heading
 * @param children the children within the heading
 * @return the heading jsx
 */
const Heading = ({
    className,
    size,
    children,
}: {
    className: string;
    size: number;
    children: ReactNode;
}): ReactElement => (
    <h1 className={cn("pt-2.5 font-bold", size >= 2 && "pt-7", className)}>
        {children}
    </h1>
);
