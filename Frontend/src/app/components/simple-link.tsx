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
import { AnchorHTMLAttributes, ReactElement, ReactNode } from "react";
import Link, { LinkProps } from "next/link";

/**
 * The props for this link.
 */
type SimpleLinkProps = {
    /**
     * Whether to use the noRef attribute.
     */
    noRef?: boolean;

    /**
     * Should the link be opened in a new tab?
     */
    newTab?: boolean;

    /**
     * The children of the link.
     */
    children: ReactNode;
};

/**
 * A wrapper for the NextJS link component.
 *
 * @return the link jsx
 */
const SimpleLink = ({
    noRef,
    newTab,
    children,
    ...props
}: AnchorHTMLAttributes<HTMLAnchorElement> &
    LinkProps &
    SimpleLinkProps): ReactElement => (
    <Link
        rel={noRef ? "noopener noreferrer" : ""}
        target={newTab ? "_blank" : "_self"}
        {...props}
    >
        {children}
    </Link>
);
export default SimpleLink;
