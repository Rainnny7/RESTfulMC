"use client";

import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import CountUp from "react-countup";
import { ReactElement } from "react";

/**
 * Props for the counter.
 */
type CounterProps = {
    /**
     * The name of the counter.
     */
    name: string;

    /**
     * The amount to count up to.
     */
    amount: number;

    /**
     * The optional duration of the count up.
     * <p>
     * Uses the default duration if not provided.
     * </p>
     */
    duration?: number | undefined;
};

/**
 * A counter component.
 *
 * @param amount the amount to count up to
 * @param duration the optional duration of the count up
 * @returns the counter jsx
 */
const Counter = ({ name, amount, duration }: CounterProps): ReactElement => (
    <div className="flex flex-col items-center gap-4">
        <h1
            className={cn(
                "text-6xl text-minecraft-green-3",
                minecrafter.className
            )}
        >
            {name}
        </h1>
        <h2 className="text-4xl font-semibold uppercase">
            <CountUp start={0} end={amount} duration={duration} />
        </h2>
    </div>
);
export default Counter;
