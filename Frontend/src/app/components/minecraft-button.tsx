import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

/**
 * A Minecraft styled button.
 *
 * @returns the button jsx
 */
const MinecraftButton = ({
	className,
	children,
	...props
}: Readonly<{
	className?: string;
	children: React.ReactNode;
}>): JSX.Element => (
	<Button
		className={cn(
			"before:absolute before:-inset-x-5 before:rotate-90 before:w-9 before:h-1 before:bg-minecraft-green-1", // Left Green Bar
			"after:absolute after:right-[-1.24rem] after:rotate-90 after:w-9 after:h-1 after:bg-minecraft-green-1", // Right Green Bar
			"relative h-11 px-5 bg-minecraft-green-2 hover:opacity-85 hover:bg-minecraft-green-2 rounded-none tracking-wide font-semibold uppercase transition-all transform-gpu", // Styling
			className
		)}
		variant="ghost"
		style={{
			// Above and below the button shadow
			boxShadow:
				"inset 0 -4px 0 hsl(var(--minecraft-green-1)), inset 0 4px 0 hsl(var(--minecraft-green-3))",
		}}
		{...props}
	>
		{children}
	</Button>
);
export default MinecraftButton;
