import { Metadata } from "next";
import { ReactElement } from "react";

export const metadata: Metadata = {
    title: "Featured Servers",
    description: "View a list of featured servers on the platform.",
};

const ServersPage = (): ReactElement => (
    <main className="px-5 min-h-screen">Servers</main>
);
export default ServersPage;
