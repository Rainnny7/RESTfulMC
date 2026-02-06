import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ReactElement } from "react";
import { CachedPlayer } from "restfulmc-lib";

const PlayerDetails = ({ player }: { player: CachedPlayer }): ReactElement => (
    <Card className="w-124">
        <CardHeader>
            <CardTitle>Player Details</CardTitle>
        </CardHeader>
        <CardContent>
            <p>Card Content</p>
        </CardContent>
    </Card>
);
export default PlayerDetails;
