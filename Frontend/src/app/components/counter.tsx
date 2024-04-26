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
 * @param name the name of the counter
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
