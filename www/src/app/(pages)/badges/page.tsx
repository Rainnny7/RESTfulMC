import { Metadata } from "next";
import { ReactElement } from "react";

export const metadata: Metadata = {
    title: "Badges",
    description: "View available badges on RESTfulMC.",
};

const BadgesPage = (): ReactElement => (
    <main className="px-5 min-h-screen">Badges</main>
);
export default BadgesPage;
