"use client";

import { useSkinProvider3D } from "@/providers/skin-provider-3d-provider";
import { skin3DAnimations } from "@/types/skin";
import { RefObject, useEffect, useRef } from "react";
import { CachedPlayer, SkinModel } from "restfulmc-lib";
import { SkinViewer } from "skinview3d";

type PlayerSkinViewer3DProps = {
    player: CachedPlayer;
};

const PlayerSkinViewer3D = ({ player }: PlayerSkinViewer3DProps) => {
    const canvasRef: RefObject<HTMLCanvasElement | null> =
        useRef<HTMLCanvasElement | null>(null);
    const { animation, showElytra, updateSkinViewerRef } = useSkinProvider3D();

    const effectiveShowElytra =
        showElytra || animation.name === skin3DAnimations.flying.name;

    useEffect(() => {
        const canvas: HTMLCanvasElement | null = canvasRef.current;
        if (!canvas) return;

        // Create the skin viewer (load cape manually to set correct backEquipment)
        const viewer: SkinViewer = new SkinViewer({
            canvas,
            width: 215,
            height: 290,
            model: player.skin.model === SkinModel.SLIM ? "slim" : "default",
            skin: player.skin.url,
            animation: animation.animation,
        });
        if (player.cape) {
            viewer.loadCape(player.cape.url, {
                backEquipment: effectiveShowElytra ? "elytra" : "cape",
            });
        }
        // viewer.controls.enableZoom = false;

        viewer.playerWrapper.rotation.y = Math.PI / 6.5; // Slightly rotate to the right by default (~30°)
        viewer.camera.position.y = 10; // Move the camera up by 20 units

        if (viewer.animation) {
            viewer.animation.speed = 1.25;
        }
        updateSkinViewerRef(viewer);

        return () => {
            viewer.dispose();
            updateSkinViewerRef(null);
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps -- isAutoRotating synced by provider's useEffect
    }, [player, animation, effectiveShowElytra, updateSkinViewerRef]);

    return (
        <canvas
            ref={canvasRef}
            className="rounded-lg cursor-grab"
            style={{ maxWidth: "100%" }}
        />
    );
};
export default PlayerSkinViewer3D;
