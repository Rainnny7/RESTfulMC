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
import { ReactElement, ReactNode } from "react";
import Sidebar from "@/components/docs/sidebar";

/**
 * Force the layout to be static.
 */
export const dynamic = "force-static";

/**
 * The layout for the docs page.
 *
 * @param params the params of the request
 * @param render the children to render
 * @returns the layout jsx
 */
const DocumentationLayout = ({
    params,
    children,
}: Readonly<{
    params: any;
    children: ReactNode;
}>): ReactElement => {
    const activeSlug: string = ((params.slug as string[]) || undefined)?.join(
        "/"
    ); // The active slug of this page
    return (
        <section className="min-h-screen py-32 pb-52 flex justify-center">
            <div className="flex flex-wrap gap-32 xl:divide-x-2">
                <Sidebar activeSlug={activeSlug} />
                {children}
            </div>
        </section>
    );
};
export default DocumentationLayout;
