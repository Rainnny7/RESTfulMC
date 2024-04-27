import createMDX from '@next/mdx'

/** @type {import('next').NextConfig} */
const nextConfig = {
	pageExtensions: ["ts", "tsx", "js", "jsx", "md", "mdx"],
	images: {
		remotePatterns: [
			{
				protocol: "https",
				hostname: "api.restfulmc.cc",
			},
			{
				protocol: "https",
				hostname: "flagcdn.com",
			},
		],
	},
	experimental: {
		mdxRs: true,
	},
};

const withMDX = createMDX({})

// Merge MDX config with Next.js config
export default withMDX(nextConfig)