import PageHeader from "@/components/page-header";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

const ServerLoading = () => (
    <main className="min-h-screen flex flex-col">
        <PageHeader
            className="min-h-112"
            contentClassName="gap-5"
            backgroundImage="/media/background/landing.webp"
        >
            {/* Server icon & hostname skeleton */}
            <div className="flex gap-5 items-center">
                <Skeleton className="size-20 rounded-xl shrink-0" />
                <Skeleton className="h-10 w-48" />
            </div>
        </PageHeader>

        <div className="-mt-16 px-5 flex flex-col gap-5 items-center">
            <Card className="w-full max-w-2xl">
                <CardContent className="pt-4">
                    <Skeleton className="min-h-[200px] w-full rounded-xl" />
                </CardContent>
            </Card>
        </div>
    </main>
);

export default ServerLoading;
