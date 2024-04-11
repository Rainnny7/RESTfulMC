/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.mc.common.renderer.impl;

import lombok.NonNull;
import me.braydon.mc.common.renderer.SkinRenderer;
import me.braydon.mc.model.skin.ISkinPart;
import me.braydon.mc.model.skin.Skin;

import java.awt.image.BufferedImage;

/**
 * A basic 2D renderer for a {@link ISkinPart.Custom#BODY}.
 *
 * @author Braydon
 */
public final class BodySkinPartRenderer extends SkinRenderer<ISkinPart.Custom> {
    public static final BodySkinPartRenderer INSTANCE = new BodySkinPartRenderer();

    /**
     * Invoke this render to render the
     * given skin part for the provided skin.
     *
     * @param skin     the skin to render the part for
     * @param part     the part to render
     * @param overlays whether to render overlays
     * @param size     the size to scale the skin part to
     * @return the rendered skin part
     */
    @Override @NonNull
    public BufferedImage render(@NonNull Skin skin, @NonNull ISkinPart.Custom part, boolean overlays, int size) {
        return getVanillaSkinPart(skin, ISkinPart.Vanilla.FACE, size);
    }
}