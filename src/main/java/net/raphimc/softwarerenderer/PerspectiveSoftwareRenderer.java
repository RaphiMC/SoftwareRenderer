/*
 * This file is part of SoftwareRenderer - https://github.com/RaphiMC/SoftwareRenderer
 * Copyright (C) 2024-2025 RK_01/RaphiMC and contributors
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

import net.raphimc.softwarerenderer.primitives.Primitive;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.util.List;

public class PerspectiveSoftwareRenderer extends SoftwareRenderer {

    private final Matrix4f screenSpaceMatrix;
    private final Matrix4f projectionMatrix;
    private final Matrix4fStack modelViewMatrix;
    private final Matrix4f finalMatrix;

    public PerspectiveSoftwareRenderer(final int width, final int height, final float fov) {
        super(width, height);

        this.screenSpaceMatrix = new Matrix4f().translate(0F, height, 0F).scale(width / 2F, -height / 2F, 1F).translate(1F, 1F, 0F);
        this.projectionMatrix = new Matrix4f().setPerspective((float) Math.toRadians(fov), (float) width / height, 0.01F, 512F);
        this.modelViewMatrix = new Matrix4fStack(32);
        this.finalMatrix = new Matrix4f();
    }

    public int draw3DPrimitives(final List<? extends Primitive> primitives) {
        this.computeFinalMatrix();
        return this.drawPrimitives(primitives, this.finalMatrix);
    }

    public Matrix4fStack getModelViewMatrix() {
        return this.modelViewMatrix;
    }

    private void computeFinalMatrix() {
        this.finalMatrix.identity().mul(this.screenSpaceMatrix).mul(this.projectionMatrix).mul(this.modelViewMatrix);
    }

}
