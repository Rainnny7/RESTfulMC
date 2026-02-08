import PageHeader from "@/components/page-header";
import { FlameIcon } from "lucide-react";
import { Metadata } from "next";
import { ReactElement } from "react";

export const metadata: Metadata = {
    title: "Capes",
    description: "View a list of all official capes in Minecraft.",
};

const CapesPage = (): ReactElement => (
    <main className="min-h-screen">
        {/* Header */}
        <PageHeader
            className="min-h-100"
            contentClassName="gap-5 text-center"
            backgroundImage="/media/background/capes.webp"
        >
            <div className="flex gap-5 items-center">
                <FlameIcon className="hidden xs:block size-10" />
                <h1 className="text-5xl font-black">Capes</h1>
            </div>
            <p className="text-xl text-muted-foreground">
                Below is a list of all official capes in Minecraft.
            </p>
        </PageHeader>
    </main>
);
export default CapesPage;
