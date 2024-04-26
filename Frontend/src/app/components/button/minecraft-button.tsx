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
import { Button } from "@/components/ui/button";
import { cn } from "@/lib";
import { ButtonHTMLAttributes, ReactElement, ReactNode } from "react";

/**
 * Props for this button.
 */
type MinecraftButtonProps = {
    /**
     * The class name to apply to this button.
     */
    className?: string;

    /**
     * The children of this button.
     */
    children: ReactNode;
};

/**
 * A Minecraft styled button.
 *
 * @returns the button jsx
 */
const MinecraftButton = ({
    className,
    children,
    ...props
}: ButtonHTMLAttributes<HTMLButtonElement> &
    MinecraftButtonProps): ReactElement => (
    <Button
        className={cn(
            "before:absolute before:-inset-x-5 before:rotate-90 before:w-9 before:h-1 before:bg-minecraft-green-1", // Left Green Bar
            "after:absolute after:right-[-1.24rem] after:rotate-90 after:w-9 after:h-1 after:bg-minecraft-green-1", // Right Green Bar
            "relative h-full px-5 bg-minecraft-green-2 hover:opacity-85 hover:bg-minecraft-green-2 rounded-none tracking-wide font-semibold uppercase transition-all transform-gpu", // Styling
            className
        )}
        variant="ghost"
        style={{
            // Above and below the button shadow
            boxShadow:
                "inset 0 -4px 0 hsl(var(--minecraft-green-1)), inset 0 4px 0 hsl(var(--minecraft-green-3))",
        }}
        {...props}
    >
        {children}
    </Button>
);
export default MinecraftButton;
