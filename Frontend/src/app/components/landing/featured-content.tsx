import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import Link from "next/link";

const FeaturedContent = (): JSX.Element => (
	<div className="-translate-y-14 flex justify-center items-center">
		<div className="grid grid-cols-2 gap-5">
			<FeaturedItem
				name="Player Lookup"
				description="Id dolore elit mollit adipisicing adipisicing."
				image="/media/featured/server.png"
				href="/player"
			/>
		</div>
	</div>
);

const FeaturedItem = ({
	name,
	description,
	image,
	href,
}: {
	name: string;
	description: string;
	image: string;
	href: string;
}): JSX.Element => (
	<Link
		className={
			"w-[19rem] h-80 flex flex-col justify-center items-center bg-[url('/media/featured/server.png')] bg-cover rounded-3xl text-center backdrop-blur-md hover:scale-[1.01] transition-all transform-gpu"
		}
		href={href}
	>
		<h1
			className={cn("text-3xl font-semibold text-white", minecrafter.className)}
		>
			{name}
		</h1>
		<h2 className="text-md max-w-[15rem]">{description}</h2>
	</Link>
);

export default FeaturedContent;
