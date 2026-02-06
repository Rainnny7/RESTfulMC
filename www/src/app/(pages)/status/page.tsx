import PageHeader from "@/components/page-header";
import { ReactElement } from "react";

const MojangStatusPage = (): ReactElement => (
    <main className="px-5 min-h-screen">
        <PageHeader
            className="min-h-112"
            contentClassName="gap-5"
            backgroundImage="/media/background/status.webp"
        >
            <h1 className="text-5xl font-black">Mojang Status</h1>
            <p className="text-xl text-muted-foreground">
                Below is the status of all Microsoft and Mojang services.
            </p>
        </PageHeader>
    </main>
);
export default MojangStatusPage;
