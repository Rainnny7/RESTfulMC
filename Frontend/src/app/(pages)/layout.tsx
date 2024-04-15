import Navbar from "@/components/navbar";
import { TooltipProvider } from "@/components/ui/tooltip";
import config from "@/config";
import ThemeProvider from "@/provider/theme-provider";
import type { Metadata, Viewport } from "next";
import PlausibleProvider from "next-plausible";
import { Noto_Sans } from "next/font/google";
import "../globals.css";

/**
 * Site metadata & viewport.
 */
export const metadata: Metadata = config.metadata;
export const viewport: Viewport = config.viewport;

/**
 * The default Minecraft font to use.
 */
const notoSans = Noto_Sans({ subsets: ["latin"] });

/**
 * The root layout for this site.
 *
 * @param children the children of this layout
 * @returns the layout jsx
 */
const RootLayout = ({
	children,
}: Readonly<{
	children: React.ReactNode;
}>): JSX.Element => {
	const analyticsDomain: string | undefined = config.analyticsDomain;
	return (
		<html lang="en" className={`scroll-smooth ${notoSans.className}`}>
			<head>
				{analyticsDomain && (
					<PlausibleProvider
						domain={analyticsDomain}
						customDomain="https://analytics.rainnny.club"
						selfHosted
					/>
				)}
			</head>
			<body className="relative min-h-screen">
				<ThemeProvider attribute="class" defaultTheme="dark">
					<TooltipProvider>
						<Navbar />
						{children}
					</TooltipProvider>
				</ThemeProvider>
			</body>
		</html>
	);
};
export default RootLayout;
