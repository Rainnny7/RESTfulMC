import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    output: "standalone",
    reactStrictMode: true,
    reactCompiler: true,
    poweredByHeader: false,
    typescript: { ignoreBuildErrors: true },
    devIndicators: false,
    transpilePackages: ["@t3-oss/env-nextjs", "@t3-oss/env-core"],
};
export default nextConfig;
