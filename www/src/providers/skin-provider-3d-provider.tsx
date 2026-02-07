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
import { SkinViewer } from "skinview3d";

type SkinProvider3DContextType = {
    /**
     * The currently selected animation.
     */
    animation: Skin3DAnimation;

    /**
     * Whether the player is auto-rotating.
     */
    isAutoRotating: boolean;

    /**
     * Update the skin viewer reference for controls.
     */
    updateSkinViewerRef: (skinViewer: SkinViewer | null) => void;

    /**
     * Play an animation.
     */
    playAnimation: (animation: Skin3DAnimation) => void;

    /**
     * Toggle auto-rotating.
     */
    toggleAutoRotating: () => void;
};

const SkinProvider3DContext = createContext<
    SkinProvider3DContextType | undefined
>(undefined);

export const SkinProvider3DProvider = ({
    children,
}: {
    children: ReactNode;
}) => {
    const skinViewerRef: RefObject<SkinViewer | null> =
        useRef<SkinViewer | null>(null);
    const [selectedAnimation, setSelectedAnimation] = useState<Skin3DAnimation>(
        skin3DAnimations.idle
    );
    const [isAutoRotating, setIsAutoRotating] = useState<boolean>(true);

    const playAnimation = (animation: Skin3DAnimation) => {
        setSelectedAnimation(animation);
    };

    // Ensure auto rotation is synced with the viewer
    useEffect(() => {
        if (skinViewerRef.current) {
            skinViewerRef.current.autoRotate = isAutoRotating;
        }
    }, [isAutoRotating]);

    const toggleAutoRotating = () => {
        if (skinViewerRef.current) {
            skinViewerRef.current.autoRotate = !isAutoRotating;
            setIsAutoRotating(!isAutoRotating);
        }
    };

    return (
        <SkinProvider3DContext.Provider
            value={{
                animation: selectedAnimation,
                isAutoRotating,
                updateSkinViewerRef: (skinViewer: SkinViewer | null) => {
                    skinViewerRef.current = skinViewer;
                },
                playAnimation,
                toggleAutoRotating,
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
