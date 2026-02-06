import NotFoundPageContent from "@/components/not-found-page-content";
import { Metadata } from "next";
import { ReactElement } from "react";

export const metadata: Metadata = {
    title: "Page Not Found",
    description: "The page you are looking for doesn't exist.",
};

const NotFoundPage = (): ReactElement => (
    <main className="min-h-screen flex justify-center items-center">
        <NotFoundPageContent />
    </main>
);
export default NotFoundPage;
