/** @type {import('next').NextConfig} */
const nextConfig = {
	images: {
		remotePatterns: [
			{
				protocol: "https",
				hostname: "mc.rainnny.club",
			},
		],
	},
};

export default nextConfig;
