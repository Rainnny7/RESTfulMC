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
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/app/common/utils";
import { FeaturedItemProps } from "@/types/config";
import { ReactElement } from "react";
import SimpleLink from "@/components/simple-link";

/**
 * The featured content component.
 *
 * @returns the featured content jsx
 */
const FeaturedContent = (): ReactElement => (
    <div className="flex justify-center items-center">
        <div className="max-w-2xl flex flex-wrap justify-center gap-5">
            {config.featuredItems.map(
                (item: FeaturedItemProps, index: number) => (
                    <FeaturedItem key={index} {...item} />
                )
            )}
        </div>
    </div>
);

/**
 * A featured item component.
 *
 * @param props the item props
 * @returns the item jsx
 */
const FeaturedItem = ({
    name,
    description,
    image,
    href,
}: FeaturedItemProps): ReactElement => (
    <SimpleLink
        className="pt-28 w-[19rem] h-80 flex flex-col gap-1 items-center bg-center bg-cover bg-no-repeat rounded-3xl text-center backdrop-blur-md hover:scale-[1.01] transition-all transform-gpu"
        href={href}
        style={{
            backgroundImage: `url(${image})`,
        }}
    >
        <h1
            className={cn(
                "text-3xl font-semibold text-white",
                minecrafter.className
            )}
        >
            {name}
        </h1>
        <h2 className="text-md max-w-[15rem]">{description}</h2>
    </SimpleLink>
);

export default FeaturedContent;
