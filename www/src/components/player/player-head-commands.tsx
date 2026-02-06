import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ReactElement } from "react";
import { CachedPlayer } from "restfulmc-lib";

const PlayerHeadCommands = ({
    player,
}: {
    player: CachedPlayer;
}): ReactElement => {
    return (
        <Card className="mx-auto w-full sm:max-w-124">
            <CardHeader>
                <CardTitle>Head Commands</CardTitle>
            </CardHeader>
            <CardContent>
                <p>Card Content</p>
            </CardContent>
        </Card>
    );
};

export default PlayerHeadCommands;
