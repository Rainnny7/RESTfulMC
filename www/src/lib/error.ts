/**
 * Extracts a user-friendly error message from an unknown error.
 * Handles RestfulMCAPIError and other error shapes.
 */
export function getErrorMessage(error: unknown, fallback: string): string {
    if (error && typeof error === "object" && "message" in error) {
        const msg = (error as { message: unknown }).message;
        return typeof msg === "string" ? msg : fallback;
    }
    return fallback;
}
