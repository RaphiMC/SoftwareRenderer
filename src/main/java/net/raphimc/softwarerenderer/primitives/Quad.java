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
package net.raphimc.softwarerenderer.primitives;

import net.raphimc.softwarerenderer.data.ClipRect;
import net.raphimc.softwarerenderer.data.ImageBuffer;
import net.raphimc.softwarerenderer.enums.CullFace;
import net.raphimc.softwarerenderer.rasterizer.Rasterizer;
import net.raphimc.softwarerenderer.rasterizer.TriangleRasterizer;
import net.raphimc.softwarerenderer.vertex.RasterVertex;
import net.raphimc.softwarerenderer.vertex.Vertex;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public record Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4, @Nullable ImageBuffer textureBuffer) implements Primitive {

    @Override
    public Rasterizer createRasterizer(final Matrix4f matrix) {
        final RasterVertex rv1 = this.v1.project(matrix);
        final RasterVertex rv2 = this.v2.project(matrix);
        final RasterVertex rv3 = this.v3.project(matrix);
        final RasterVertex rv4 = this.v4.project(matrix);
        final TriangleRasterizer rasterizer1 = new TriangleRasterizer(rv1, rv2, rv3, this.textureBuffer);
        final TriangleRasterizer rasterizer2 = new TriangleRasterizer(rv3, rv4, rv1, this.textureBuffer);
        return new QuadRasterizer(rasterizer1, rasterizer2);
    }

    private record QuadRasterizer(TriangleRasterizer rasterizer1, TriangleRasterizer rasterizer2) implements Rasterizer {

        @Override
        public void rasterize(final ImageBuffer colorBuffer, final float @Nullable [] depthBuffer, final @Nullable ClipRect clipRect) {
            this.rasterizer1.rasterize(colorBuffer, depthBuffer, clipRect);
            this.rasterizer2.rasterize(colorBuffer, depthBuffer, clipRect);
        }

        @Override
        public boolean canBeCulled(final CullFace cullFace) {
            return this.rasterizer1.canBeCulled(cullFace) && this.rasterizer2.canBeCulled(cullFace);
        }

    }

}
