/*
 * This file is part of SoftwareRenderer - https://github.com/RaphiMC/SoftwareRenderer
 * Copyright (C) 2024-2024 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.softwarerenderer;

import net.raphimc.softwarerenderer.data.ClipRect;
import net.raphimc.softwarerenderer.data.ImageBuffer;
import net.raphimc.softwarerenderer.enums.CullFace;
import net.raphimc.softwarerenderer.primitives.Primitive;
import net.raphimc.softwarerenderer.rasterizer.Rasterizer;
import org.joml.Matrix4f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class SoftwareRenderer {

    private final BufferedImage renderTarget;
    private final ImageBuffer colorBuffer;
    private final Graphics2D graphics;
    private final float[] depthBuffer;
    private final Matrix4f identityMatrix;

    private CullFace cullFace = CullFace.NONE;
    private boolean depthEnabled = false;
    private ClipRect clipRect = null;
    private boolean wireframe = false;

    public SoftwareRenderer(final int width, final int height) {
        this.renderTarget = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        this.colorBuffer = new ImageBuffer(this.renderTarget);
        this.graphics = this.renderTarget.createGraphics();
        this.depthBuffer = new float[width * height];
        this.identityMatrix = new Matrix4f();
        this.clearDepthBuffer();
    }

    public void clearColorBuffer() {
        Arrays.fill(this.colorBuffer.raster(), 0);
    }

    public void clearDepthBuffer() {
        Arrays.fill(this.depthBuffer, Float.MAX_VALUE);
    }

    public int draw2DPrimitives(final List<? extends Primitive> primitives) {
        this.identityMatrix.identity();
        return this.drawPrimitives(primitives, this.identityMatrix);
    }

    public int drawPrimitives(final List<? extends Primitive> primitives, final Matrix4f matrix) {
        int renderedPrimitives = 0;
        for (Primitive primitive : primitives) {
            final Rasterizer rasterizer = primitive.createRasterizer(matrix);
            if (this.rasterize(rasterizer)) {
                renderedPrimitives++;
            }
        }
        return renderedPrimitives;
    }

    public boolean rasterize(final Rasterizer rasterizer) {
        if (rasterizer.canBeCulled(this.cullFace)) {
            return false;
        }
        if (this.wireframe) {
            rasterizer.drawWireframe(this.graphics, this.clipRect);
        } else {
            rasterizer.rasterize(this.colorBuffer, this.depthEnabled ? this.depthBuffer : null, this.clipRect);
        }
        return true;
    }

    public void setCullFace(final CullFace cullFace) {
        this.cullFace = cullFace;
    }

    public CullFace getCullFace() {
        return this.cullFace;
    }

    public void setDepthEnabled(final boolean depthEnabled) {
        this.depthEnabled = depthEnabled;
    }

    public boolean isDepthEnabled() {
        return this.depthEnabled;
    }

    public void setClipRect(final ClipRect clipRect) {
        this.clipRect = clipRect;
    }

    public ClipRect getClipRect() {
        return this.clipRect;
    }

    public void setWireframe(final boolean wireframe) {
        this.wireframe = wireframe;
    }

    public boolean isWireframe() {
        return this.wireframe;
    }

    public BufferedImage getImage() {
        return this.renderTarget;
    }

    public Graphics2D getGraphics2D() {
        return this.graphics;
    }

}
