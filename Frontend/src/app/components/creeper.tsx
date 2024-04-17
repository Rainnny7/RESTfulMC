import Image from "next/image";
import { ReactElement } from "react";

/**
 * A creeper image.
 *
 * @returns the creeper jsx
 */
const Creeper = (): ReactElement => (
    <Image
        src="/media/creeper.png"
        alt="A Minecraft Creeper"
        width={216}
        height={216}
    />
);
export default Creeper;
