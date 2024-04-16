import Creeper from "@/components/creeper";
import { minecrafter } from "@/font/fonts";
import { cn } from "@/lib/utils";

/**
 * The 404 page.
 *
 * @returns the page jsx
 */
const NotFoundPage = (): JSX.Element => (
	<main className="h-[84vh] flex flex-col gap-3 justify-center items-center">
		{/* Creeper */}
		<Creeper />

		{/* Header */}
		<h1
			className={cn("text-6xl text-minecraft-green-3", minecrafter.className)}
		>
			We&apos;re Sssssorry
		</h1>

		{/* Error */}
		<h2 className="text-2xl">
			The page you were looking for could not be found.
		</h2>
	</main>
);
export default NotFoundPage;