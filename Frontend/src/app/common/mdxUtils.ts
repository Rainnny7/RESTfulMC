import * as fs from "node:fs";
import path from "node:path";

/**
 * The regex to match for metadata.
 */
const METADATA_REGEX: RegExp = /---\s*([\s\S]*?)\s*---/;

/**
 * Get the content to
 * display in the docs.
 */
export const getDocsContent = (): DocsContentMetadata[] =>
    getMetadata<DocsContentMetadata>(
        path.join(process.cwd(), "src", "markdown", "docs")
    );

/**
 * Get the metadata of mdx
 * files in the given directory.
 *
 * @param directory the directory to search
 */
export const getMetadata = <T extends MDXMetadata>(directory: string): T[] => {
    const files: string[] = fs
        .readdirSync(directory)
        .filter((file: string): boolean => path.extname(file) === ".mdx"); // Read the MDX files
    return files.map((file: string): T => {
        const filePath: string = path.join(directory, file); // The path of the file
        return {
            ...parseMetadata<T>(fs.readFileSync(filePath, "utf-8")),
            slug: path.basename(file, path.extname(file)),
        }; // Map each file to its metadata
    });
};

/**
 * Parse the metadata from
 * the given content.
 *
 * @param content the content to parse
 * @returns the metadata and content
 * @template T the type of metadata
 */
const parseMetadata = <T extends MDXMetadata>(content: string): T => {
    const metadataBlock: string = METADATA_REGEX.exec(content)![1]; // Get the block of metadata
    content = content.replace(METADATA_REGEX, "").trim(); // Remove the metadata block from the content
    let metadata: Partial<{
        [key: string]: string;
    }> = {}; // The metadata to return

    // Parse the metadata block as a key-value pair
    metadataBlock
        .trim() // Trim any leading or trailing whitespace
        .split("\n") // Get each line
        .forEach((line: string): void => {
            const split: string[] = line.split(": "); // Split the metadata by the colon
            let value: string = split[1].trim(); // The value of the metadata
            value = value.replace(/^['"](.*)['"]$/, "$1"); // Remove quotes
            metadata[split[0].trim()] = value; // Add the metadata to the object
        });

    // Return the metadata and content. The initial
    // slug is empty, and is defined later on.
    return { ...metadata, content } as T;
};
