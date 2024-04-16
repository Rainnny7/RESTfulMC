/**
 * Props for a page.
 */
export type PageProps = {
	params: { slug: string };
	searchParams: { [key: string]: string | string[] | undefined };
};
