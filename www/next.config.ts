import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    output: "standalone",
    reactStrictMode: true,
    images: {
        remotePatterns: [
            { protocol: "https", hostname: "textures.minecraft.net", pathname: "/**" },
            { protocol: "http", hostname: "s.optifine.net", pathname: "/capes/**" },
        ],
    },
    reactCompiler: true,
    poweredByHeader: false,
    typescript: { ignoreBuildErrors: true },
    devIndicators: false,
    transpilePackages: ["@t3-oss/env-nextjs", "@t3-oss/env-core"],
};
export default nextConfig;
