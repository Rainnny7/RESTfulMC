/**
 * Capitalize the first
 * character in the given input.
 *
 * @param input the input to capitalize
 * @returns the capitalized input
 */
export const capitalize = (input: string): string => {
    return input.charAt(0).toUpperCase() + input.slice(1);
};
