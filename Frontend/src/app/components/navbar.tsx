import MinecraftButton from "@/components/minecraft-button";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import { StarIcon } from "@heroicons/react/24/outline";
import Image from "next/image";
import Link from "next/link";

/**
 * The navbar for the site.
 *
 * @returns the navbar jsx
 */
const Navbar = (): JSX.Element => (
	<nav className="fixed inset-x-0 flex h-16 px-12 justify-center sm:justify-between items-center bg-navbar-background">
		{/* Left */}
		<div className="flex gap-7 lg:gap-12 items-center transition-all transform-gpu">
			{/* App Branding */}
			<Link
				className={cn(
					"text-3xl text-minecraft-green-3 hover:opacity-85 transition-all transform-gpu",
					minecrafter.className
				)}
				href="/"
			>
				{/* Small Screens */}
				<Image
					className="flex md:hidden"
					src="/media/logo.webp"
					alt="Site Logo"
					width={42}
					height={42}
				/>

				{/* Large Screens */}
				<span className="hidden md:flex">{config.siteName}</span>
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
		<div className="hidden sm:flex">
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
