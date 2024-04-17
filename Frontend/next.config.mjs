/** @type {import('next').NextConfig} */
const nextConfig = {
	images: {
		remotePatterns: [
			{
				protocol: "https",
				hostname: "api.restfulmc.cc",
			},
		],
	},
};

export default nextConfig;
