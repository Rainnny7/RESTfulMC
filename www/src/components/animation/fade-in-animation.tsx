"use client";

import { cn } from "@/lib/utils";
import { motion } from "motion/react";
import type { ReactElement, ReactNode } from "react";

type Direction = "top" | "bottom" | "left" | "right";

type FadeInAnimationProps = {
    className?: string;
    delay?: number;
    direction?: Direction;
    offset?: number;
    viewport?: boolean;
    viewportOnce?: boolean;
    viewportMargin?: string;
    children: ReactNode;
};

const getInitialPosition = (direction: Direction, offset: number) => {
    switch (direction) {
        case "top":
            return { opacity: 0, y: -offset };
        case "bottom":
            return { opacity: 0, y: offset };
        case "left":
            return { opacity: 0, x: -offset };
        case "right":
            return { opacity: 0, x: offset };
        default:
            return { opacity: 0, y: -offset };
    }
};

const FadeInAnimation = ({
    className,
    delay = 0.1,
    direction = "top",
    offset = 20,
    viewport = false,
    viewportOnce = true,
    viewportMargin = "-100px",
    children,
}: FadeInAnimationProps): ReactElement => (
    <motion.div
        className={cn(className)}
        initial={getInitialPosition(direction, offset)}
        animate={viewport ? undefined : { opacity: 1, x: 0, y: 0 }}
        whileInView={viewport ? { opacity: 1, x: 0, y: 0 } : undefined}
        transition={{ duration: 0.5, delay }}
        viewport={
            viewport
                ? { once: viewportOnce, margin: viewportMargin }
                : undefined
        }
    >
        {children}
    </motion.div>
);
export default FadeInAnimation;
