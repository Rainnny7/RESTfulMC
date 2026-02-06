"use client";

import { Button } from "@/components/ui/button";
import {
    Empty,
    EmptyContent,
    EmptyDescription,
    EmptyHeader,
    EmptyMedia,
    EmptyTitle,
} from "@/components/ui/empty";
import { HomeIcon } from "lucide-react";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { ReactElement } from "react";

const NotFoundPageContent = (): ReactElement => {
    const path: string = usePathname();
    return (
        <Empty>
            <EmptyHeader>
                <EmptyMedia variant="default">
                    <Image
                        src="/media/creeper.webp"
                        alt="RESTfulMC"
                        width={128}
                        height={220}
                        draggable={false}
                    />
                </EmptyMedia>
                <EmptyTitle>Page Not Found</EmptyTitle>
                <EmptyDescription>
                    The page{" "}
                    <span className="text-primary font-medium">{path}</span>{" "}
                    doesn&apos;t exist or has been moved.
                </EmptyDescription>
            </EmptyHeader>
            <EmptyContent>
                <Button variant="outline" asChild>
                    <Link href="/">
                        <HomeIcon className="size-4" />
                        <span>Go Home</span>
                    </Link>
                </Button>
            </EmptyContent>
        </Empty>
    );
};
export default NotFoundPageContent;
