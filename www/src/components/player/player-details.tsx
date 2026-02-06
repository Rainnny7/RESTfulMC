import SimpleLink from "@/components/simple-link";
import SimpleTooltip from "@/components/simple-tooltip";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { formatTimeAgo } from "@/lib/date";
import { env } from "@/lib/env";
import { ReactElement, ReactNode } from "react";
import { CachedPlayer } from "restfulmc-lib";

const PlayerDetails = ({ player }: { player: CachedPlayer }): ReactElement => {
    const isCached: boolean = player.cached !== -1;
    return (
        <Card className="w-full sm:max-w-124">
            <CardHeader>
                <CardTitle>Player Details</CardTitle>
            </CardHeader>
            <CardContent className="flex flex-col gap-2">
                {/* UUIDs */}
                <PlayerDetailElement
                    label="Unique ID"
                    value={
                        <div className="flex flex-col gap-1">
                            <span>{player.uniqueId}</span>
                            <span>{player.uniqueId.replace(/-/g, "")}</span>
                        </div>
                    }
                />

                {/* Username */}
                <PlayerDetailElement label="Username" value={player.username} />

                {/* Legacy Player? */}
                <PlayerDetailElement
                    label="Legacy"
                    value={
                        <Badge
                            variant={!  player.legacy ? "success" : "destructive"}
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
                                View
                            </Button>
                        </SimpleLink>
                    }
                />
            </CardContent>
        </Card>
    );
};

const PlayerDetailElement = ({
    label,
    value,
}: {
    label: string;
    value: string | ReactNode;
}): ReactElement => (
    <div className="flex gap-6 items-start">
        <span className="text-sm font-medium min-w-[100px] shrink-0">
            {label}
        </span>
        <div className="text-sm text-muted-foreground">{value}</div>
    </div>
);

export default PlayerDetails;
