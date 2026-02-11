import CopyButton from "@/components/copy-button";
import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { formatTimeAgo } from "@/lib/date";
import { env } from "@/lib/env";
import { cn } from "@/lib/utils";
import { ExternalLinkIcon, InfoIcon } from "lucide-react";
import { ReactElement, ReactNode } from "react";
import { CachedPlayer } from "restfulmc-lib";

const PlayerDetails = ({ player }: { player: CachedPlayer }): ReactElement => {
    const isCached: boolean = player.cached !== -1;
    return (
        <Card className="mx-auto w-full sm:max-w-156">
            <CardHeader>
                <CardTitle>Player Details</CardTitle>
            </CardHeader>
            <CardContent className="flex flex-col gap-2">
                {/* UUIDs */}
                <PlayerDetailElement
                    className="font-mono"
                    label="Unique ID"
                    value={player.uniqueId}
                />

                {/* Username */}
                <PlayerDetailElement
                    label="Username"
                    value={
                        <div className="flex gap-1.5 items-center">
                            <span>{player.username}</span>
                            <CopyButton
                                value={player.username}
                                variant="ghost"
                                size="icon-xs-2"
                                copyMessage="Copied username to clipboard"
                            />
                        </div>
                    }
                />

                {/* Legacy Player? */}
                <PlayerDetailElement
                    label="Legacy"
                    tooltip="A player that does not have an email associated with their account (e.g: a Minecraft account using username and password around 2013-2016)."
                    value={
                        <Badge
                            variant={!player.legacy ? "success" : "destructive"}
                        >
                            {player.legacy ? "Yes" : "No"}
                        </Badge>
                    }
                />

                {/* Cached Status */}
                <PlayerDetailElement
                    label="Cached"
                    value={
                        <SimpleTooltip
                            content={
                                isCached ? (
                                    <p>
                                        You&apos;re viewing a cached response of
                                        the player
                                    </p>
                                ) : (
                                    <p>
                                        You&apos;re viewing the latest data for
                                        the player.
                                    </p>
                                )
                            }
                            side="bottom"
                        >
                            <Badge
                                variant={isCached ? "success" : "destructive"}
                            >
                                {!isCached
                                    ? "No"
                                    : formatTimeAgo(
                                          new Date(player.cached),
                                          true
                                      )}
                            </Badge>
                        </SimpleTooltip>
                    }
                />

                {/* View Raw API Response */}
                <PlayerDetailElement
                    label="API Response"
                    value={
                        <SimpleLink
                            href={`${env.NEXT_PUBLIC_API_URL}/player/${player.uniqueId}`}
                        >
                            <Button variant="outline" size="xs">
                                <span>View</span>
                                <ExternalLinkIcon />
                            </Button>
                        </SimpleLink>
                    }
                />
            </CardContent>
        </Card>
    );
};

const PlayerDetailElement = ({
    className,
    label,
    tooltip,
    value,
}: {
    className?: string;
    label: string;
    tooltip?: string;
    value: string | ReactNode;
}): ReactElement => (
    <div className="flex gap-6 items-start">
        <span className="flex gap-1.5 items-center text-sm font-medium min-w-[100px] shrink-0">
            {label}
            {tooltip && (
                <SimpleTooltip content={tooltip} side="bottom">
                    <InfoIcon className="size-3.5 text-muted-foreground hover:opacity-75 transition-opacity duration-300 ease-in-out transform-gpu" />
                </SimpleTooltip>
            )}
        </span>
        <div className={cn("text-sm text-muted-foreground", className)}>
            {value}
        </div>
    </div>
);

export default PlayerDetails;
