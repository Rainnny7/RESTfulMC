"use client";

import StatusAlert from "@/components/landing/status-alert";
import LookupForm from "@/components/lookup-form";
import PageHeader from "@/components/page-header";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import ShinyText from "@/components/ui/shiny-text";
import { ReactElement, useEffect, useState } from "react";

const LOADING_MESSAGES = [
    "Loading...",
    "Fetching data...",
    "Hold on...",
    "Almost there...",
    "Mining for data...",
    "Checking the server...",
    "Looking up player...",
    "Searching the block...",
    "Loading chunks...",
    "Pinging server...",
    "One moment...",
    "Gathering data...",
    "Please wait...",
];

const HeroSection = (): ReactElement => {
    const [isLookupFormFetching, setIsLookupFormFetching] =
        useState<boolean>(false);
    const [lookupError, setLookupError] = useState<string | undefined>(
        undefined
    );
    const [loadingMessage, setLoadingMessage] = useState(LOADING_MESSAGES[0]);

    useEffect(() => {
        if (!isLookupFormFetching) {
            return;
        }
        setLoadingMessage(
            LOADING_MESSAGES[
                Math.floor(Math.random() * LOADING_MESSAGES.length)
            ]
        );
    }, [isLookupFormFetching]);

    return (
        <div className="relative">
            <div className="absolute inset-x-5 top-24 flex justify-center z-10">
                <StatusAlert />
            </div>

            <PageHeader
                contentClassName="gap-2"
                backgroundImage="/media/background/landing.webp"
            >
                <Card className="w-full max-w-xl bg-card/45 backdrop-blur-md">
                    <CardHeader>
                        <CardTitle>Minecraft Player / Server Lookup</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <LookupForm
                            placeholder="Enter a Username / UUID / Server IP"
                            isFetching={isLookupFormFetching}
                            error={lookupError}
                            setIsFetching={setIsLookupFormFetching}
                            setError={setLookupError}
                        />
                    </CardContent>
                    <CardFooter className="flex flex-col gap-2 items-start">
                        {isLookupFormFetching ? (
                            <ShinyText
                                text={loadingMessage}
                                speed={1.5}
                                delay={0}
                                color="#b5b5b5"
                                shineColor="#ffffff"
                                spread={35}
                                direction="left"
                            />
                        ) : lookupError ? (
                            <p className="text-destructive text-sm">
                                {lookupError}
                            </p>
                        ) : (
                            <p className="text-muted-foreground">
                                Enter a Username, UUID, or Server IP to get
                                started.
                            </p>
                        )}
                    </CardFooter>
                </Card>
            </PageHeader>
        </div>
    );
};
export default HeroSection;
