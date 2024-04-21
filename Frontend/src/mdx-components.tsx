import { ReactElement, ReactNode } from "react";
import { MDXRemote } from "remote-mdx/rsc";

const components: any = {
    h1: ({ children }: { children: ReactNode }) => (
        <h1 className="text-3xl">{children}</h1>
    ),
};

export const CustomMDX = (props: any): ReactElement => {
    return (
        <MDXRemote
            {...props}
            components={{
                ...components,
                ...(props.components || {}),
            }}
        />
    );
};
