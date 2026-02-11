export type Config = {
    sourceCodeUrl: string;
    documentationUrl: string;
    socials: SocialLink[];
};

export type SocialLink = {
    logo: string;
    tooltip: string;
    href: string;
};
