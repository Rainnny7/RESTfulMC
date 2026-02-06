"use client";

import LookupForm from "@/components/lookup-form";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import ShinyText from "@/components/ui/shiny-text";
import { cn } from "@/lib/utils";
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
        <div className="relative min-h-172 flex flex-col justify-center items-center overflow-hidden">
            {/* Background */}
            <div className="absolute inset-0 mask-[linear-gradient(to_bottom,black_25%,transparent)]">
                <div
                    className={cn(
                        "h-full w-full bg-cover bg-center bg-no-repeat",
                        "mask-[radial-gradient(ellipse_at_center,black_30%,transparent_100%)]"
                    )}
                    style={{
                        backgroundImage: `url(/media/background/landing.webp)`,
                    }}
                />

                {/* Darker at bottom to blend with background */}
                <div className="absolute inset-0 bg-linear-to-b from-transparent via-transparent to-background" />
            </div>

            {/* Player / Server Lookup */}
            <section className="relative px-5 w-full flex flex-col gap-2 items-center z-10">
                <Card className="w-full max-w-lg bg-card/45 backdrop-blur-md">
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
            </section>
        </div>
    );
};
export default HeroSection;
