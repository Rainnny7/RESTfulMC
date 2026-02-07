"use client";

import CopyButton from "@/components/copy-button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { ReactElement, useState } from "react";
import { CachedPlayer } from "restfulmc-lib";

const HEAD_COMMANDS: Record<string, string> = {
    "1.20.5": '/give @p minecraft:player_head[profile={name:"<player>"}]',
    "1.13": '/give @p minecraft:player_head{SkullOwner:"<player>"}',
    "1.8": '/give @p minecraft:skull 1 3 {SkullOwner:"<player>"}',
};

const PlayerHeadCommands = ({
    player,
}: {
    player: CachedPlayer;
}): ReactElement => {
    const [selectedVersion, setSelectedVersion] = useState<string>(
        Object.keys(HEAD_COMMANDS)[0]
    );
    const headCommand: string = HEAD_COMMANDS[selectedVersion].replace(
        "<player>",
        player.username
    );

    return (
        <Card className="mx-auto w-full sm:max-w-124">
            <CardHeader>
                <CardTitle>Head Commands</CardTitle>
            </CardHeader>
            <CardContent className="flex gap-1.5 items-center">
                {/* Version Selector */}
                <Select
                    value={selectedVersion}
                    onValueChange={(value) => setSelectedVersion(value)}
                >
                    <SelectTrigger className="w-24">
                        <SelectValue placeholder={selectedVersion} />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            {Object.keys(HEAD_COMMANDS).map((version) => (
                                <SelectItem key={version} value={version}>
                                    {version}
                                </SelectItem>
                            ))}
                        </SelectGroup>
                    </SelectContent>
                </Select>

                {/* Head Command */}
                <Input type="text" value={headCommand} readOnly />

                {/* Copy Button */}
                <CopyButton
                    variant="frosted"
                    size="icon"
                    value={headCommand}
                    copyMessage={`Copied ${selectedVersion} head command for ${player.username}:`}
                />
            </CardContent>
        </Card>
    );
};

export default PlayerHeadCommands;
