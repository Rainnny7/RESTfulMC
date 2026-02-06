import PageHeader from "@/components/page-header";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
} from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

const PlayerLoading = () => (
    <main className="min-h-screen flex flex-col">
        <PageHeader
            className="min-h-112"
            contentClassName="gap-7"
            backgroundImage="/media/background/player.webp"
        >
            {/* Player Head & Username skeleton */}
            <div className="px-5 flex gap-5 items-center">
                <Skeleton className="size-[86px] rounded-md shrink-0" />
                <Skeleton className="h-12 w-48" />
            </div>

            {/* UUID skeleton */}
            <div className="px-5 w-full flex justify-center gap-1.5 items-center">
                <Skeleton className="h-9 w-full max-w-xs rounded-lg" />
                <Skeleton className="size-8 rounded-lg shrink-0" />
            </div>
        </PageHeader>

        <div className="-mt-16 px-5 flex flex-col-reverse lg:flex-row justify-center gap-5 items-center lg:items-start">
            <div className="w-full sm:w-auto flex flex-col sm:flex-row lg:flex-col gap-5">
                {/* Skin Preview skeleton */}
                <Card className="w-full sm:w-60">
                    <CardHeader>
                        <Skeleton className="h-6 w-28" />
                    </CardHeader>
                    <CardContent className="relative h-60 flex justify-center">
                        <Skeleton className="size-36 rounded-md" />
                    </CardContent>
                    <CardFooter className="flex flex-wrap justify-center gap-1">
                        {Array.from({ length: 5 }).map((_, i) => (
                            <Skeleton key={i} className="size-8 rounded-md" />
                        ))}
                    </CardFooter>
                </Card>

                {/* Cape Preview skeleton */}
                <Card className="w-full sm:w-60 h-fit">
                    <CardHeader>
                        <Skeleton className="h-6 w-24" />
                    </CardHeader>
                    <CardContent className="relative h-40 flex justify-center">
                        <Skeleton className="size-36 rounded-md" />
                    </CardContent>
                </Card>
            </div>

            <div className="flex flex-col gap-5">
                {/* Player Details skeleton */}
                <Card className="w-full sm:max-w-124">
                    <CardHeader>
                        <Skeleton className="h-6 w-28" />
                    </CardHeader>
                    <CardContent className="flex flex-col gap-2">
                        {Array.from({ length: 5 }).map((_, i) => (
                            <div
                                key={i}
                                className="flex gap-6 items-start"
                            >
                                <Skeleton className="h-4 w-24 shrink-0" />
                                <Skeleton className="h-4 flex-1 max-w-48" />
                            </div>
                        ))}
                    </CardContent>
                </Card>

                {/* Head Commands skeleton */}
                <Card>
                    <CardHeader>
                        <Skeleton className="h-6 w-32" />
                    </CardHeader>
                    <CardContent>
                        <Skeleton className="h-20 w-full rounded-md" />
                    </CardContent>
                </Card>
            </div>
        </div>
    </main>
);

export default PlayerLoading;
