import type { MDXComponents } from "mdx/types";

export const useMDXComponents = (components: MDXComponents): MDXComponents => {
    return {
        h1: ({ children }) => (
            <h1 className="text-6xl font-semibold">{children}</h1>
        ),
        ...components,
    };
};
