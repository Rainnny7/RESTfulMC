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
import { ReactElement } from "react";
import { cn } from "@/app/common/utils";

/**
 * The background hero component.
 *
 * @returns the background jsx
 */
const HeroBackground = (): ReactElement => (
    <div
        className={cn(
            "before:absolute before:left-0 before:top-0 before:w-full before:h-full before:bg-black/65", // Dark Overlay
            `absolute top-0 left-0 w-full h-full bg-[url("/media/background.jpg")] bg-cover bg-center -z-10`, // Background
            "after:absolute after:left-0 after:bottom-0 after:w-full after:h-1 after:bg-border" // Bottom Border
        )}
    />
);
export default HeroBackground;
