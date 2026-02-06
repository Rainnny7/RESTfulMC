import { Metadata } from "next";
import { ReactElement } from "react";

export const metadata: Metadata = {
    title: "Capes",
    description: "View all Minecraft capes on RESTfulMC.",
};

const CapesPage = (): ReactElement => (
    <main className="px-5 min-h-screen">Capes</main>
);
export default CapesPage;
