import Image from "next/image";

/**
 * A creeper image.
 *
 * @returns the creeper jsx
 */
const Creeper = (): JSX.Element => (
	<Image
		src="/media/creeper.png"
		alt="A Minecraft Creeper"
		width={216}
		height={216}
	/>
);
export default Creeper;
