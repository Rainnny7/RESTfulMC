import MinecraftButton from "@/components/minecraft-button";
import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import Link from "next/link";

/**
 * The hero content.
 *
 * @returns the hero jsx
 */
const Hero = (): JSX.Element => (
	<div className="h-[70vh] flex flex-col gap-8 justify-center items-center">
		<div className="flex flex-col gap-4 items-center text-center">
			{/* Title */}
			<h1
				className={cn("text-5xl text-minecraft-green-3", minecrafter.className)}
			>
				{config.siteName}
			</h1>

			{/* Subtitle */}
			<h2 className="text-xl">{config.metadata.description}</h2>
		</div>

		{/* Links */}
		<div className="flex gap-10">
			<MinecraftButton className="w-44 h-12">
				<Link href="/docs">Get Started</Link>
			</MinecraftButton>
		</div>
	</div>
);
export default Hero;
