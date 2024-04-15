import config from "@/config";
import localFont from "next/font/local";
import Link from "next/link";

/**
 * The title font to use to brand the site.
 */
const minecrafter = localFont({
	src: "../font/Minecrafter.ttf",
});

/**
 * The navbar for the site.
 *
 * @returns the navbar jsx
 */
const Navbar = () => (
	<nav className="fixed inset-x-0 h-16 px-10 flex justify-between items-center bg-navbar-background uppercase">
		{/* Links */}
		<div className={`relative flex gap-5`}>
			{Object.entries(config.navbarLinks).map((link, index) => (
				<Link key={index} href={link[1]}>
					{link[0]}
				</Link>
			))}
		</div>

		{/* Site Branding */}
		<div>
			<Link
				className={`text-3xl text-minecraft-green-1 ${minecrafter.className}`}
				href="/"
			>
				{config.siteName}
			</Link>
		</div>

		<div>Three</div>
	</nav>
);
export default Navbar;
