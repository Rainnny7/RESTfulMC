import { CircleAlert, CircleCheck, CircleX, LucideIcon } from "lucide-react";

export type ServiceStatus = {
    name: string;
    description: string;
    icon: LucideIcon;
    color: string;
};

export const serviceStatuses: Record<string, ServiceStatus> = {
    online: {
        name: "Operational",
        description: "The service is operational and functioning as expected.",
        icon: CircleCheck,
        color: "text-green-500",
    },
    degraded: {
        name: "Degraded Performance",
        description: "The service is experiencing degraded performance.",
        icon: CircleAlert,
        color: "text-yellow-500",
    },
    offline: {
        name: "Outage",
        description: "The service is experiencing an outage.",
        icon: CircleX,
        color: "text-red-500",
    },
};
