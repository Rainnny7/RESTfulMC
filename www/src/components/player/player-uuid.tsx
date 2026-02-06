"use client";

import CopyButton from "@/components/copy-button";
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { ClipboardIcon } from "lucide-react";
import { ReactElement, useState } from "react";
import { CachedPlayer } from "restfulmc-lib";

type UUIDFormat = {
    name: string;
    format: (uuid: string) => string;
};

const UUID_FORMATS: UUIDFormat[] = [
    {
        name: "Plain",
        format: (uuid: string) => uuid,
    },
    {
        name: "Trimmed",
        format: (uuid: string) => uuid.replace(/-/g, ""),
    },
];

const PlayerUUID = ({ player }: { player: CachedPlayer }): ReactElement => {
    const [uuidFormat, setUUIDFormat] = useState<UUIDFormat>(UUID_FORMATS[0]);
    return (
        <div className="px-5 w-full flex justify-center gap-1.5 items-center">
            {/* UUID Format */}
            <Select
                value={uuidFormat.name}
                onValueChange={(value) =>
                    setUUIDFormat(
                        UUID_FORMATS.find(
                            (format: UUIDFormat) => format.name === value
                        )!
                    )
                }
            >
                <SelectTrigger className="w-full max-w-xs">
                    <SelectValue
                        placeholder={uuidFormat.format(player.uniqueId)}
                    />
                </SelectTrigger>
                <SelectContent>
                    <SelectGroup>
                        {UUID_FORMATS.map((format: UUIDFormat) => (
                            <SelectItem key={format.name} value={format.name}>
                                {format.format(player.uniqueId)}
                            </SelectItem>
                        ))}
                    </SelectGroup>
                </SelectContent>
            </Select>

            {/* Copy Button */}
            <CopyButton
                variant="frosted"
                size="icon"
                copyMessage={`Copied ${player.username}'s ${uuidFormat.name} UUID:`}
                value={uuidFormat.format(player.uniqueId)}
            >
                <ClipboardIcon className="size-4" />
            </CopyButton>
        </div>
    );
};
export default PlayerUUID;
