import PageHeader from "@/components/page-header";
import {
    Card,
    CardContent,
    CardHeader,
} from "@/components/ui/card";
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
                <CardHeader>
                    <Skeleton className="h-6 w-40" />
                    <Skeleton className="h-4 w-64 mt-2" />
                </CardHeader>
                <CardContent className="flex flex-col gap-4">
                    {Array.from({ length: 4 }).map((_, i) => (
                        <div key={i} className="flex gap-4 items-center">
                            <Skeleton className="h-4 w-24 shrink-0" />
                            <Skeleton className="h-4 flex-1" />
                        </div>
                    ))}
                </CardContent>
            </Card>
        </div>
    </main>
);

export default ServerLoading;
