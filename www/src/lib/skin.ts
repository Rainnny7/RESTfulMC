import { SkinPart } from "restfulmc-lib";

export const formatSkinPartName = (part: SkinPart): string => {
    return part
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(/\b\w/g, (c) => c.toUpperCase());
};