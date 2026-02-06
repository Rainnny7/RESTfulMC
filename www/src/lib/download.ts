/**
 * Downloads a file from a URL with the given filename.
 * Falls back to opening in a new tab if fetch fails (e.g. CORS).
 */
export const downloadFile = async (
    url: string,
    filename: string
): Promise<void> => {
    try {
        const blob: Blob = await (await fetch(url)).blob();
        const objectUrl: string = URL.createObjectURL(blob);
        const link: HTMLAnchorElement = Object.assign(
            document.createElement("a"),
            {
                href: objectUrl,
                download: filename,
            }
        );
        link.click();
        URL.revokeObjectURL(objectUrl);
    } catch {
        window.open(url, "_blank");
    }
};
