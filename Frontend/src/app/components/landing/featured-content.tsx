import config from "@/config";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";
import { FeaturedItemProps } from "@/types/config";
import Link from "next/link";

/**
 * The featured content component.
 *
 * @returns the featured content jsx
 */
const FeaturedContent = (): JSX.Element => (
	<div className="flex justify-center items-center">
		<div className="max-w-2xl flex flex-wrap justify-center gap-5">
			{config.featuredItems.map((item, index) => (
				<FeaturedItem key={index} {...item} />
			))}
		</div>
	</div>
);

/**
 * A featured item component.
 *
 * @param props the item props
 * @returns the item jsx
 */
const FeaturedItem = ({
	name,
	description,
	image,
	href,
}: FeaturedItemProps): JSX.Element => (
	<Link
		className="pt-28 w-[19rem] h-80 flex flex-col gap-1 items-center bg-center bg-cover bg-no-repeat rounded-3xl text-center backdrop-blur-md hover:scale-[1.01] transition-all transform-gpu"
		href={href}
		style={{
			backgroundImage: `url(${image})`,
		}}
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
