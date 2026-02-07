import { ReactElement, ReactNode } from "react";

type CodeProps = {
    children: ReactNode;
};

const Code = ({ children }: CodeProps): ReactElement => (
    <code className="px-2 py-0.5 bg-primary/20 text-xs font-medium text-white/85 font-mono border border-primary/80 rounded-[min(var(--radius-md),10px)]">
        {children}
    </code>
);
export default Code;
