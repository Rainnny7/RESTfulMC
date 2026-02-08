import PageHeader from "@/components/page-header";
import ServiceStatus from "@/components/status/service-status";
import StatusKey from "@/components/status/status-key";
import { ActivityIcon } from "lucide-react";
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
            className="min-h-100"
            contentClassName="gap-5 text-center"
            backgroundImage="/media/background/status.webp"
        >
            <div className="flex gap-5 items-center">
                <ActivityIcon className="hidden xs:block size-10" />
                <h1 className="text-5xl font-black">Mojang Status</h1>
            </div>
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
