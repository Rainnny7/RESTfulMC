"use client";

import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { ReactElement } from "react";

const MojangServiceStatus = (): ReactElement => {
    return (
        <Card className="w-full max-w-xl">
            <CardHeader>
                <CardTitle>Service Status</CardTitle>
                <CardDescription>
                    Below is the status of all Microsoft and Mojang services.
                </CardDescription>
            </CardHeader>
            <CardContent>
                <p>Content</p>
            </CardContent>
        </Card>
    );
};
export default MojangServiceStatus;
