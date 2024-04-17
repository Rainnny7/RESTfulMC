"use client";

import GitHubStarCount from "@/components/github-star-count";
import MinecraftButton from "@/components/minecraft-button";
import { Skeleton } from "@/components/ui/skeleton";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import { StarIcon } from "@heroicons/react/24/outline";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { Suspense } from "react";

/**
 * The navbar for the site.
 *
 * @returns the navbar jsx
 */
const Navbar = (): JSX.Element => {
	const path: string = usePathname(); // Get the current path
	return (
		<nav className="fixed inset-x-0 flex h-16 px-12 justify-center sm:justify-between items-center bg-navbar-background z-50">
			{/* Left */}
			<div className="flex gap-7 lg:gap-12 items-center transition-all transform-gpu">
				{/* App Branding */}
				<Link
					className={cn(
						"hidden sm:flex text-3xl text-minecraft-green-3 hover:opacity-85 transition-all transform-gpu",
						minecrafter.className
					)}
					href="/"
				>
					{/* Small Screens */}
					<Image
						className="hidden sm:flex lg:hidden"
						src="/media/logo.webp"
						alt="Site Logo"
						width={42}
						height={42}
					/>

					{/* Large Screens */}
					<span className="hidden lg:flex">{config.siteName}</span>
				</Link>

				{/* Links */}
				<div className="flex gap-7">
					{Object.entries(config.navbarLinks).map((link, index) => {
						const url: string = link[1]; // The href of the link
						let active: boolean = path.startsWith(url); // Is this the active link?
						return (
							<Link
								key={index}
								className={cn(
									"font-semibold uppercase hover:text-minecraft-green-4 transition-all transform-gpu",
									active && "text-minecraft-green-4"
								)}
								href={url}
							>
								{link[0]}
							</Link>
						);
					})}
				</div>
			</div>

			{/* Social Buttons - Right */}
			<div className="hidden md:flex">
				{/* Star on Github <3 */}
				<Link
					href="https://github.com/Rainnny7/RESTfulMC"
					rel="noopener noreferrer"
					target="_blank"
				>
					<MinecraftButton className="flex gap-1.5 items-center group/star">
						{/* Star Count */}
						<Suspense fallback={<Skeleton className="w-4 h-5 rounded-md" />}>
							<GitHubStarCount />
						</Suspense>

						<StarIcon
							className="group-hover/star:text-orange-400 delay-0 transition-all transform-gpu"
							width={22}
							height={22}
						/>
						<span>Star on GitHub</span>
					</MinecraftButton>
				</Link>
			</div>
		</nav>
	);
};
export default Navbar;
