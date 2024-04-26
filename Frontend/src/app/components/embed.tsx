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
import { Metadata } from "next";

/**
 * Props for an embed.
 */
type EmbedProps = {
    /**
     * The title of the embed.
     */
    title: string;

    /**
     * The description of the embed.
     */
    description: string;

    /**
     * The optional thumbnail image of the embed.
     */
    thumbnail?: string;
};

/**
 * An embed for a page.
 *
 * @param props the embed props
 * @returns the embed jsx
 */
const Embed = ({
    title,
    description,
    thumbnail = "",
}: EmbedProps): Metadata => {
    return {
        title: title,
        openGraph: {
            title: `${title}`,
            description: description,
            images: [
                {
                    url: thumbnail,
                },
            ],
        },
        twitter: {
            card: "summary",
        },
    };
};
export default Embed;
