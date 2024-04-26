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
import CodeHighlighter from "@/components/code/code-highlighter";

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
    }): ReactElement => (
        <CodeHighlighter
            className="max-w-5xl"
            language={className?.replace("language-", "")}
        >
            {children}
        </CodeHighlighter>
    ),
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
