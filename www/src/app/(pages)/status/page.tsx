import PageHeader from "@/components/page-header";
import ServiceStatus from "@/components/status/service-status";
import StatusKey from "@/components/status/status-key";
import { Metadata } from "next";
import { ReactElement } from "react";

export const metadata: Metadata = {
    title: "Mojang Service Status",
    description: "View the status of all Microsoft and Mojang services.",
};

export const dynamic = "force-dynamic";

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
        <div className="-mt-16 px-5 flex flex-col-reverse md:flex-row justify-center items-center md:items-start gap-5">
            <ServiceStatus />
            <StatusKey />
        </div>
    </main>
);
export default MojangStatusPage;
