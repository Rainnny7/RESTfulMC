import { NextFont } from "next/dist/compiled/@next/font";
import { Noto_Sans } from "next/font/google";
import localFont from "next/font/local";

/**
 * The default font to use for the site.
 */
export const notoSans: NextFont = Noto_Sans({ subsets: ["latin"] });

/**
 * The Minecraft font to use for the site.
 */
export const minecrafter: NextFont = localFont({
    src: "../font/Minecrafter.ttf",
});

export const minecraft: NextFont = localFont({
    src: "../font/Minecraft.otf",
});
