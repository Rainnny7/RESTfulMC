"use client";

import SimpleLink from "@/components/simple-link";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { useQuery } from "@tanstack/react-query";
import { AlertCircleIcon } from "lucide-react";
import { ReactElement } from "react";
import {
    getMojangServerStatus,
    MojangServer,
    MojangServerStatus,
    MojangServerStatusResponse,
} from "restfulmc-lib";

const StatusAlert = (): ReactElement | undefined => {
    const { data } = useQuery<MojangServerStatusResponse>({
        queryKey: ["status"],
        queryFn: async () => await getMojangServerStatus(),
    });
    if (!data) {
        return undefined;
    }
    const offlineServices: MojangServer[] = data.servers.filter(
        (server) => server.status === MojangServerStatus.OFFLINE
    );
    if (offlineServices.length === 0) {
        return undefined;
    }
    return (
        <SimpleLink className="hover:opacity-100" href="/status">
            <Alert className="max-w-xl" variant="destructive">
                <AlertCircleIcon />
                <AlertTitle>Status Alert</AlertTitle>
                <AlertDescription>
                    <p>
                        <b>{offlineServices.length}</b> Mojang services are
                        experiencing issues:
                    </p>
                    <span>
                        {offlineServices
                            .map((server) => server.name)
                            .join(", ")}
                    </span>
                </AlertDescription>
            </Alert>
        </SimpleLink>
    );
};
export default StatusAlert;
