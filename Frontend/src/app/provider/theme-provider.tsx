"use client";

import { ThemeProvider as NextThemesProvider } from "next-themes";
import { type ThemeProviderProps } from "next-themes/dist/types";

/**
 * The provider of themes!!
 */
const ThemeProvider = ({ children, ...props }: ThemeProviderProps) => (
	<NextThemesProvider {...props}>{children}</NextThemesProvider>
);
export default ThemeProvider;
