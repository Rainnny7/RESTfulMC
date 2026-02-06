"use client";

import { Button } from "@/components/ui/button";
import { CheckIcon, ClipboardIcon } from "lucide-react";
import { AnimatePresence, motion } from "motion/react";
import { ComponentProps, MouseEvent, useRef, useState } from "react";
import { toast } from "sonner";

const COPIED_DURATION_MS = 2000;

type CopyButtonProps = ComponentProps<typeof Button> & {
    value: string;
    copyMessage: string;
};

const CopyButton = ({
    value,
    copyMessage,
    onClick,
    children,
    ...props
}: CopyButtonProps) => {
    const [copied, setCopied] = useState<boolean>(false);
    const timeoutRef = useRef<ReturnType<typeof setTimeout> | undefined>(
        undefined
    );

    const handleClick = async (event: MouseEvent<HTMLButtonElement>) => {
        try {
            await navigator.clipboard.writeText(value);
            if (!copied) {
                toast.success(copyMessage, {
                    description: <code>{value}</code>,
                });
            }
            setCopied(true);
            clearTimeout(timeoutRef.current);
            timeoutRef.current = setTimeout(
                () => setCopied(false),
                COPIED_DURATION_MS
            );
        } catch {
            toast.error("Failed to copy to clipboard");
        }
        onClick?.(event);
    };

    return (
        <Button onClick={handleClick} {...props}>
            <AnimatePresence mode="wait">
                {copied ? (
                    <motion.span
                        key="check"
                        className="inline-flex"
                        initial={{ opacity: 0, scale: 0.5 }}
                        animate={{ opacity: 1, scale: 1 }}
                        exit={{ opacity: 0, scale: 0.5 }}
                        transition={{ duration: 0.1 }}
                    >
                        <CheckIcon className="size-4 text-green-500" />
                    </motion.span>
                ) : (
                    <motion.span
                        key="clipboard"
                        className="inline-flex"
                        initial={{ opacity: 0, scale: 0.5 }}
                        animate={{ opacity: 1, scale: 1 }}
                        exit={{ opacity: 0, scale: 0.5 }}
                        transition={{ duration: 0.1 }}
                    >
                        {children ?? <ClipboardIcon className="size-4" />}
                    </motion.span>
                )}
            </AnimatePresence>
        </Button>
    );
};
export default CopyButton;
