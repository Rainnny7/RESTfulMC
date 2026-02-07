import {
    CrouchAnimation,
    FlyingAnimation,
    IdleAnimation,
    PlayerAnimation,
    WalkingAnimation,
} from "skinview3d";

export type Skin3DAnimation = {
    name: string;
    animation: PlayerAnimation;
};

const sneakingAnimation = new CrouchAnimation();
sneakingAnimation.runOnce = true;

export const skin3DAnimations: Record<string, Skin3DAnimation> = {
    idle: {
        name: "idle",
        animation: new IdleAnimation(),
    },
    walking: {
        name: "walking",
        animation: new WalkingAnimation(),
    },
    flying: {
        name: "flying",
        animation: new FlyingAnimation(),
    },
    sneaking: {
        name: "sneaking",
        animation: sneakingAnimation,
    },
};
