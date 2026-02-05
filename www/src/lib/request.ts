import axios, { type AxiosRequestConfig, type AxiosResponse } from "axios";

type RequestReturns = "text" | "json" | "arraybuffer";

export type RequestOptions = AxiosRequestConfig & {
    next?: {
        revalidate?: number;
    };
    searchParams?: Record<string, unknown>;
    throwOnError?: boolean;
    returns?: RequestReturns;
};

const DEBUG = false;
const DEFAULT_OPTIONS: RequestOptions = {
    next: {
        revalidate: 120, // 2 minutes
    },
    throwOnError: true,
    returns: "json",
};

class Request {
    private static pendingRequests = new Map<string, Promise<unknown>>();

    private static getCacheKey(
        url: string,
        method: string,
        options?: RequestOptions
    ): string {
        return JSON.stringify({ url, method, options });
    }

    private static buildUrl(
        baseUrl: string,
        searchParams?: Record<string, unknown>
    ): string {
        if (!searchParams) return baseUrl;

        const params = new URLSearchParams(
            Object.entries(searchParams).map(([key, value]) => [
                key,
                String(value),
            ])
        );
        return `${baseUrl}?${params.toString()}`;
    }

    private static async executeRequest<T>(
        url: string,
        method: "GET" | "POST" | "PATCH",
        options: RequestOptions
    ): Promise<T | undefined> {
        try {
            const response: AxiosResponse = await axios({
                url,
                method,
                ...options,
                timeout: 7000,
                responseType: options.returns,
            });

            this.checkRateLimit(url, response);
            return response.data as T;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                if (options.throwOnError) {
                    throw new Error(`Failed to request ${url}`, {
                        cause: error.response,
                    });
                }
                return undefined;
            }
            throw error;
        }
    }

    private static checkRateLimit(url: string, response: AxiosResponse): void {
        // const rateLimit = response.headers["x-ratelimit-remaining"];
        // if (rateLimit) {
        //     const remaining = Number(rateLimit);
        //     if (remaining < 100) {
        //         console.warn(`Rate limit for ${url} remaining: ${remaining}`);
        //     }
        // }
    }

    static async send<T>(
        url: string,
        method: "GET" | "POST" | "PATCH",
        options?: RequestOptions
    ): Promise<T | undefined> {
        const finalOptions = { ...DEFAULT_OPTIONS, ...options };
        const finalUrl = this.buildUrl(url, finalOptions.searchParams);
        const cacheKey = this.getCacheKey(finalUrl, method, finalOptions);

        if (DEBUG) {
            console.info(`Sending request: ${finalUrl}`);
        }

        // Check for pending requests
        const pendingRequest = this.pendingRequests.get(cacheKey);
        if (pendingRequest) {
            return pendingRequest as Promise<T | undefined>;
        }

        const requestPromise = this.executeRequest<T>(
            finalUrl,
            method,
            finalOptions
        );
        this.pendingRequests.set(cacheKey, requestPromise);

        try {
            return await requestPromise;
        } finally {
            if (DEBUG) {
                console.info(`Request completed: ${finalUrl}`);
            }
            this.pendingRequests.delete(cacheKey);
        }
    }

    static async get<T>(
        url: string,
        options?: RequestOptions
    ): Promise<T | undefined> {
        return this.send<T>(url, "GET", options);
    }

    static async patch<T>(
        url: string,
        options?: RequestOptions
    ): Promise<T | undefined> {
        return this.send<T>(url, "PATCH", options);
    }

    static async post<T>(
        url: string,
        options?: RequestOptions
    ): Promise<T | undefined> {
        return this.send<T>(url, "POST", options);
    }
}

// Export the public API
const request = {
    send: Request.send.bind(Request),
    get: Request.get.bind(Request),
    post: Request.post.bind(Request),
    patch: Request.patch.bind(Request),
};

export default request;