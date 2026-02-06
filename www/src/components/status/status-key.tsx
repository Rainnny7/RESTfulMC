import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";
import { ServiceStatus, serviceStatuses } from "@/types/status";
import { LucideIcon } from "lucide-react";
import { ReactElement } from "react";

const StatusKey = (): ReactElement => (
    <Card className="w-full md:max-w-2xs h-fit">
        <CardHeader>
            <CardTitle>Status Key</CardTitle>
            <CardDescription>
                Defines possible statuses for a service.
            </CardDescription>
        </CardHeader>
        <CardContent className="flex flex-col gap-1.5">
            {Object.entries(serviceStatuses).map(
                ([key, value]: [string, ServiceStatus]) => {
                    const Icon: LucideIcon = value.icon;
                    return (
                        <div key={key} className="flex gap-2 items-center">
                            <Icon className={cn("size-4", value.color)} />
                            <p>{value.name}</p>
                        </div>
                    );
                }
            )}
        </CardContent>
    </Card>
);
export default StatusKey;
