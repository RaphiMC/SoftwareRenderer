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

import net.raphimc.softwarerenderer.data.ImageBuffer;
import net.raphimc.softwarerenderer.rasterizer.Rasterizer;
import net.raphimc.softwarerenderer.rasterizer.TriangleRasterizer;
import net.raphimc.softwarerenderer.vertex.Vertex;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public record Triangle(Vertex v1, Vertex v2, Vertex v3, @Nullable ImageBuffer textureBuffer) implements Primitive {

    @Override
    public Rasterizer createRasterizer(final Matrix4f matrix) {
        return new TriangleRasterizer(this.v1.project(matrix), this.v2.project(matrix), this.v3.project(matrix), this.textureBuffer);
    }

}
