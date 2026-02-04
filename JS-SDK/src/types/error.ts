/**
 * A response representing an error.
 */
export type RestfulMCAPIError = {
    /**
     * The status of this error.
     */
    status: string;

    /**
     * The HTTP code of this error.
     */
    code: number;

    /**
     * The message of this error.
     */
    message: string;

    /**
     * The timestamp this error occurred.
     */
    timestamp: string;
};
