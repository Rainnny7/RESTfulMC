import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";

const Hero = (): JSX.Element => (
	<div className="h-[85vh] flex flex-col gap-4 justify-center items-center pointer-events-none">
		{/* Title */}
		<h1
			className={cn("text-5xl text-minecraft-green-3", minecrafter.className)}
		>
			{config.siteName}
		</h1>

		{/* Subtitle */}
		<h2 className="text-xl">{config.metadata.description}</h2>
	</div>
);
export default Hero;
