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
package net.raphimc.softwarerenderer.vertex;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public record FloatVertex(float x, float y, float z, int c, float u, float v) implements Vertex {

    public FloatVertex(final float x, final float y, final float z, final int c) {
        this(x, y, z, c, 0, 0);
    }

    @Override
    public RasterVertex project(final Matrix4f matrix) {
        final Vector4f vec4f = new Vector4f(this.x, this.y, this.z, 1F).mul(matrix);
        return new RasterVertex(vec4f.x / vec4f.w, vec4f.y / vec4f.w, vec4f.z / vec4f.w, vec4f.w, this.c, this.u, this.v);
    }

}
