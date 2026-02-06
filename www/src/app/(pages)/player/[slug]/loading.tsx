import PageHeader from "@/components/page-header";
import { Card, CardContent } from "@/components/ui/card";
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
                    <CardContent className="pt-4">
                        <Skeleton className="h-[280px] w-full rounded-lg" />
                    </CardContent>
                </Card>

                {/* Cape Preview skeleton */}
                <Card className="w-full sm:w-60 h-fit">
                    <CardContent className="pt-4">
                        <Skeleton className="h-[220px] w-full rounded-lg" />
                    </CardContent>
                </Card>
            </div>

            <div className="w-full flex flex-col gap-5 lg:w-auto lg:min-w-124">
                {/* Player Details skeleton */}
                <Card className="w-full sm:max-w-124">
                    <CardContent className="pt-4">
                        <Skeleton className="min-h-[240px] w-full rounded-lg" />
                    </CardContent>
                </Card>

                {/* Head Commands skeleton */}
                <Card className="w-full sm:max-w-124">
                    <CardContent className="pt-4">
                        <Skeleton className="h-[120px] w-full rounded-lg" />
                    </CardContent>
                </Card>
            </div>
        </div>
    </main>
);

export default PlayerLoading;
