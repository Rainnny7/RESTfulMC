"use client";

import { useEffect, useRef } from "react";
import { CachedPlayer, SkinModel } from "restfulmc-lib";
import { IdleAnimation, SkinViewer } from "skinview3d";

type PlayerSkinViewer3DProps = {
    player: CachedPlayer;
};

const PlayerSkinViewer3D = ({ player }: PlayerSkinViewer3DProps) => {
    const canvasRef = useRef<HTMLCanvasElement | null>(null);
    const skinViewerRef = useRef<SkinViewer | null>(null);

    useEffect(() => {
        const canvas: HTMLCanvasElement | null = canvasRef.current;
        if (!canvas) return;

        // Create the skin viewer
        const viewer: SkinViewer = new SkinViewer({
            canvas,
            width: 280,
            height: 272,
            model: player.skin.model === SkinModel.SLIM ? "slim" : "default",
            skin: player.skin.url,
            cape: player.cape?.url ?? undefined,
            nameTag: player.username,
            zoom: 0.8,
            animation: new IdleAnimation(),
        });
        viewer.controls.enableZoom = false;

        // Slightly rotate the player to the right by default (~15°)
        viewer.playerWrapper.rotation.y = Math.PI / 10;

        // viewer.autoRotate = true;
        if (viewer.animation) {
            viewer.animation.speed = 1.5;
        }
        skinViewerRef.current = viewer;

        return () => {
            viewer.dispose();
            skinViewerRef.current = null;
        };
    }, [player]);

    return (
        <canvas
            ref={canvasRef}
            className="rounded-lg cursor-grab"
            style={{ maxWidth: "100%" }}
        />
    );
};
export default PlayerSkinViewer3D;
