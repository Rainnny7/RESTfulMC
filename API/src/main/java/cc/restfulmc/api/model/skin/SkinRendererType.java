package cc.restfulmc.api.model.skin;

import cc.restfulmc.api.common.renderer.SkinRenderer;
import cc.restfulmc.api.common.renderer.impl.skin.BodyRenderer;
import cc.restfulmc.api.common.renderer.impl.skin.FaceRenderer;
import cc.restfulmc.api.common.renderer.impl.skin.HeadRenderer;
import cc.restfulmc.api.common.renderer.impl.skin.fullbody.FullBodyRendererBack;
import cc.restfulmc.api.common.renderer.impl.skin.fullbody.FullBodyRendererFront;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author Braydon
 */
@AllArgsConstructor @Getter
public enum SkinRendererType {
    FACE(FaceRenderer.INSTANCE),
    HEAD(HeadRenderer.INSTANCE),
    FULLBODY_FRONT(FullBodyRendererFront.INSTANCE),
    FULLBODY_BACK(FullBodyRendererBack.INSTANCE),
    BODY(BodyRenderer.INSTANCE);

    @NonNull private final SkinRenderer renderer;
}
