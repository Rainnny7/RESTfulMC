/**
 * Capitalize the first
 * character in the given input.
 *
 * @param input the input to capitalize
 * @returns the capitalized input
 */
export const capitialize = (input: string): string => {
    return input.charAt(0).toUpperCase() + input.slice(1);
};
