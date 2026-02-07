"use client";

import AppLogo from "@/components/app-logo";
import LookupForm from "@/components/lookup-form";
import PageHeader from "@/components/page-header";
import ShinyLoadingText from "@/components/shiny-loading-text";
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
        <PageHeader
            contentClassName="gap-10"
            backgroundImage="/media/background/landing.webp"
        >
            {/* Header */}
            <div className="flex flex-col gap-2 items-center text-center">
                <div className="flex gap-4 items-center">
                    <AppLogo size={56} />
                    <h1 className="text-5xl font-black">RESTfulMC</h1>
                </div>
                <p className="text-xl text-muted-foreground">
                    A powerful RESTful API for Minecraft utilizing Springboot.
                </p>
            </div>

            {/* Lookup Form */}
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
                        <ShinyLoadingText text={loadingMessage} />
                    ) : lookupError ? (
                        <p className="text-destructive text-sm">
                            {lookupError}
                        </p>
                    ) : (
                        <p className="text-muted-foreground">
                            Enter a Username, UUID, or Server IP to get started.
                        </p>
                    )}
                </CardFooter>
            </Card>
        </PageHeader>
    );
};
export default HeroSection;
