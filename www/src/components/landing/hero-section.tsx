"use client";

import LookupForm from "@/components/lookup-form";
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";
import { ReactElement, useState } from "react";

const HeroSection = (): ReactElement => {
    const [lookupError, setLookupError] = useState<string | undefined>(
        undefined
    );
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
                        backgroundImage: `url(/media/landing.webp)`,
                    }}
                />

                {/* Darker at bottom to blend with background */}
                <div className="absolute inset-0 bg-linear-to-b from-transparent via-transparent to-background" />
            </div>

            {/* Player / Server Lookup */}
            <section className="relative w-full flex flex-col gap-2 items-center z-10">
                <Card className="w-full max-w-lg bg-card/45 backdrop-blur-md">
                    <CardHeader>
                        <CardTitle>Minecraft Player / Server Lookup</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <LookupForm
                            placeholder="Enter a Username / UUID / Server IP"
                            error={lookupError}
                            setError={setLookupError}
                        />
                    </CardContent>
                    <CardFooter className="flex flex-col gap-2 items-start">
                        {lookupError ? (
                            <p className="text-destructive text-sm">
                                {lookupError}
                            </p>
                        ) : (
                            <p className="text-muted-foreground">
                                Enter a Username / UUID / Server IP to get
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
