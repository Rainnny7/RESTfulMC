import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ReactElement, ReactNode } from "react";
import { CachedPlayer } from "restfulmc-lib";

const PlayerDetails = ({ player }: { player: CachedPlayer }): ReactElement => (
    <Card className="w-124">
        <CardHeader>
            <CardTitle>Player Details</CardTitle>
        </CardHeader>
        <CardContent className="flex flex-col gap-2">
            <PlayerDetailElement
                label="Unique ID"
                value={
                    <div className="flex flex-col gap-1">
                        <span>{player.uniqueId}</span>
                        <span>{player.uniqueId.replace(/-/g, "")}</span>
                    </div>
                }
            />
            <PlayerDetailElement label="Username" value={player.username} />
            <PlayerDetailElement
                label="API Response"
                value={
                    <Button variant="outline" size="xs">
                        View
                    </Button>
                }
            />
        </CardContent>
    </Card>
);

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
