import PageHeader from "@/components/page-header";
import MojangServiceStatus from "@/components/status/mojang-service-status";
import { ReactElement } from "react";

const MojangStatusPage = (): ReactElement => (
    <main className="min-h-screen">
        {/* Header */}
        <PageHeader
            className="min-h-112"
            contentClassName="gap-5 text-center"
            backgroundImage="/media/background/status.webp"
        >
            <h1 className="text-5xl font-black">Mojang Status</h1>
            <p className="text-xl text-muted-foreground">
                Below is the status of all Microsoft and Mojang services.
            </p>
        </PageHeader>

        {/* Status */}
        <div className="-mt-16 px-5 flex justify-center">
            <MojangServiceStatus />
        </div>
    </main>
);
export default MojangStatusPage;
