import MinecraftButton from "@/components/minecraft-button";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import { StarIcon } from "@heroicons/react/24/outline";
import Link from "next/link";

/**
 * The navbar for the site.
 *
 * @returns the navbar jsx
 */
const Navbar = (): JSX.Element => (
	<nav className="fixed inset-x-0 flex h-16 px-12 justify-between items-center bg-navbar-background">
		{/* Left */}
		<div className="flex gap-16 items-center">
			{/* App Branding */}
			<Link
				className={cn(
					"text-3xl text-minecraft-green-3 hover:opacity-85 transition-all transform-gpu",
					minecrafter.className
				)}
				href="/"
			>
				{config.siteName}
			</Link>

			{/* Links */}
			<div className="flex gap-7">
				{Object.entries(config.navbarLinks).map((link, index) => (
					<Link
						key={index}
						className="font-semibold uppercase hover:text-minecraft-green-4 transition-all transform-gpu"
						href={link[1]}
					>
						{link[0]}
					</Link>
				))}
			</div>
		</div>

		{/* Social Buttons */}
		<div>
			{/* Star on Github <3 */}
			<MinecraftButton>
				<Link
					className="flex gap-1.5 items-center"
					href="https://github.com/Rainnny7/RESTfulMC"
				>
					<StarIcon width={22} height={22} />
					<span>Star on GitHub</span>
				</Link>
			</MinecraftButton>
		</div>
	</nav>
);
export default Navbar;
