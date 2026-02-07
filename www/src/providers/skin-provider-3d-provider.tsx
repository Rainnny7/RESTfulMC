"use client";

import { Skin3DAnimation, skin3DAnimations } from "@/types/skin";
import {
    createContext,
    RefObject,
    useContext,
    useEffect,
    useRef,
    useState,
    type ReactNode,
} from "react";
import { CachedPlayer } from "restfulmc-lib";
import { SkinViewer } from "skinview3d";

type SkinProvider3DContextType = {
    /**
     * The currently selected animation.
     */
    animation: Skin3DAnimation;

    /**
     * Whether to show the elytra layer.
     */
    showElytra: boolean;

    /**
     * Whether to show outter skin layers.
     */
    showLayers: boolean;

    /**
     * Update the skin viewer reference for controls.
     */
    updateSkinViewerRef: (skinViewer: SkinViewer | null) => void;

    /**
     * Play an animation.
     */
    playAnimation: (animation: Skin3DAnimation) => void;

    /**
     * Toggle showing the elytra layer.
     */
    toggleShowElytra: () => void;

    /**
     * Toggle showing outter skin layers.
     */
    toggleShowLayers: () => void;
};

const SkinProvider3DContext = createContext<
    SkinProvider3DContextType | undefined
>(undefined);

export const SkinProvider3DProvider = ({
    player,
    children,
}: {
    player: CachedPlayer;
    children: ReactNode;
}) => {
    const skinViewerRef: RefObject<SkinViewer | null> =
        useRef<SkinViewer | null>(null);
    const [selectedAnimation, setSelectedAnimation] = useState<Skin3DAnimation>(
        skin3DAnimations.idle
    );
    const [showElytra, setShowElytra] = useState<boolean>(false);
    const [showLayers, setShowLayers] = useState<boolean>(true);

    const playAnimation = (animation: Skin3DAnimation) => {
        setSelectedAnimation(animation);
    };

    // Ensure showing outter skin layers is synced with the viewer
    useEffect(() => {
        if (skinViewerRef.current) {
            skinViewerRef.current.playerObject.skin.setOuterLayerVisible(
                showLayers
            );
        }
    }, [showLayers]);

    useEffect(() => {
        if (skinViewerRef.current && player.cape) {
            skinViewerRef.current.playerObject.backEquipment = showElytra
                ? "elytra"
                : "cape";
        }
    }, [showElytra]);

    const toggleShowLayers = () => {
        if (skinViewerRef.current) {
            skinViewerRef.current.playerObject.skin.setOuterLayerVisible(
                !showLayers
            );
            setShowLayers(!showLayers);
        }
    };

    return (
        <SkinProvider3DContext.Provider
            value={{
                animation: selectedAnimation,
                showElytra,
                showLayers,
                updateSkinViewerRef: (skinViewer: SkinViewer | null) => {
                    skinViewerRef.current = skinViewer;
                },
                playAnimation,
                toggleShowElytra: () => setShowElytra((prev) => !prev),
                toggleShowLayers,
            }}
        >
            {children}
        </SkinProvider3DContext.Provider>
    );
};

export const useSkinProvider3D = () => {
    const context = useContext(SkinProvider3DContext);
    if (!context) {
        throw new Error(
            "useSkinProvider3D must be used within a SkinProvider3DProvider"
        );
    }
    return context;
};
