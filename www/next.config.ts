import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    output: "standalone",
    reactStrictMode: true,
    reactCompiler: true,
    poweredByHeader: false,
    typescript: { ignoreBuildErrors: true },
    devIndicators: false,
    images: {
        qualities: [85, 100],
        formats: ["image/webp"],
    },
    logging: {
        fetches: {
            fullUrl: true,
        },
    },
    transpilePackages: ["@t3-oss/env-nextjs", "@t3-oss/env-core"],
};
export default nextConfig;
