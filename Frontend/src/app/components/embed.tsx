import { Metadata } from "next";

/**
 * Props for an embed.
 */
type EmbedProps = {
    /**
     * The title of the embed.
     */
    title: string;

    /**
     * The color of this embed, undefined
     * for no custom color.
     * // TODO: make this work lol
     */
    color?: string | undefined;

    /**
     * The description of the embed.
     */
    description: string;

    /**
     * The optional thumbnail image of the embed.
     */
    thumbnail?: string;
};

/**
 * An embed for a page.
 *
 * @param props the embed props
 * @returns the embed jsx
 */
const Embed = ({
    title,
    color,
    description,
    thumbnail = "",
}: EmbedProps): Metadata => {
    return {
        title: title,
        openGraph: {
            title: `${title}`,
            description: description,
            images: [
                {
                    url: thumbnail,
                },
            ],
        },
        twitter: {
            card: "summary",
        },
    };
};
export default Embed;
