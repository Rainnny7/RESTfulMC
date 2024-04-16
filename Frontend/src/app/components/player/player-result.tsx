/* eslint-disable @next/next/no-img-element */
import Image from "next/image";
import Link from "next/link";
import { CachedPlayer, SkinPart } from "restfulmc-lib";

/**
 * The result of a player search.
 *
 * @param player the player to display
 * @returns the player result jsx
 */
const PlayerResult = ({
	player: {
		uniqueId,
		username,
		skin: { parts },
	},
}: {
	player: CachedPlayer;
}): JSX.Element => (
	<div className="px-2 py-3 flex flex-col gap-3 items-center bg-muted rounded-xl divide-y divide-zinc-700">
		{/* Details */}
		<div className="flex gap-5 items-center">
			{/* Player Head */}
			<Image
				src={parts[SkinPart.HEAD]}
				alt={`${username}'s Head`}
				width={128}
				height={128}
			/>

			{/* Name & Unique ID */}
			<div className="flex flex-col gap-1.5">
				<h1 className="text-xl font-bold text-minecraft-green-3">{username}</h1>
				<code className="text-zinc-300">{uniqueId}</code>
			</div>
		</div>

		{/* Skin Parts */}
		<div className="pt-3 w-[90%] flex flex-col gap-3">
			{/* Header */}
			<h1 className="font-semibold uppercase">Skin Parts</h1>

			{/* Skin Parts */}
			<div className="flex gap-5">
				{Object.entries(parts)
					.filter(
						([part]) =>
							part === SkinPart.HEAD ||
							part === SkinPart.FACE ||
							part === SkinPart.BODY_FLAT
					)
					.map(([part, url], index) => (
						<Link key={index} href={url} target="_blank">
							<img
								className="h-28 hover:scale-[1.02] transition-all transform-gpu"
								src={url}
								alt={`${username}'s ${part}`}
							/>
						</Link>
					))}
			</div>
		</div>
	</div>
);
export default PlayerResult;
