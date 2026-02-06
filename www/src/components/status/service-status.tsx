"use client";

import ShinyLoadingText from "@/components/shiny-loading-text";
import { Button } from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {
    Collapsible,
    CollapsibleContent,
    CollapsibleTrigger,
} from "@/components/ui/collapsible";
import { Skeleton } from "@/components/ui/skeleton";
import { Spinner } from "@/components/ui/spinner";
import { cn } from "@/lib/utils";
import type { ServiceStatus } from "@/types/status";
import { serviceStatuses } from "@/types/status";
import { useQuery } from "@tanstack/react-query";
import { ChevronDownIcon, LucideIcon } from "lucide-react";
import { ReactElement } from "react";
import {
    getMojangServerStatus,
    MojangServer,
    MojangServerStatusResponse,
} from "restfulmc-lib";

const ServiceStatus = (): ReactElement => {
    const { isLoading, data } = useQuery<MojangServerStatusResponse>({
        queryKey: ["status"],
        queryFn: () => getMojangServerStatus(),
    });
    return (
        <Card className="w-full md:max-w-xl">
            <CardHeader className="relative">
                <CardTitle>Service Status</CardTitle>
                <CardDescription>
                    Below is the status of all Microsoft and Mojang services.
                </CardDescription>
                {isLoading && (
                    <Spinner className="absolute top-2.5 right-2.5 text-muted-foreground" />
                )}
            </CardHeader>
            <CardContent className="flex flex-col gap-2">
                {isLoading ? (
                    <>
                        <ShinyLoadingText text="Fetching service status..." />
                        <Skeleton className="w-full h-32" />
                    </>
                ) : (
                    data?.servers.map((server: MojangServer) => {
                        const status: ServiceStatus = Object.entries(
                            serviceStatuses
                        ).find(
                            ([key]: [string, ServiceStatus]) =>
                                key === server.status.toLowerCase()
                        )![1];
                        const StatusIcon: LucideIcon = status.icon;
                        return (
                            <Collapsible key={server.name}>
                                <CollapsibleTrigger asChild>
                                    <Button
                                        className="group w-full justify-start"
                                        variant="outline"
                                    >
                                        <StatusIcon
                                            className={cn(
                                                "size-4",
                                                status.color
                                            )}
                                        />
                                        <span className="font-medium">
                                            {server.name}
                                        </span>
                                        <ChevronDownIcon className="ml-auto text-muted-foreground group-data-[state=open]:rotate-180 transition-transform duration-200 ease-in-out transform-gpu" />
                                    </Button>
                                </CollapsibleTrigger>
                                <CollapsibleContent className="mt-1 p-2 bg-card border border-border rounded-lg">
                                    {status.description}
                                </CollapsibleContent>
                            </Collapsible>
                        );
                    })
                )}
            </CardContent>
        </Card>
    );
};
export default ServiceStatus;
