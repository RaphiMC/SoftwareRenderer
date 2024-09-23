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
package net.raphimc.softwarerenderer.util;

import net.raphimc.softwarerenderer.data.ImageBuffer;
import net.raphimc.softwarerenderer.primitives.Quad;
import net.raphimc.softwarerenderer.primitives.Triangle;
import net.raphimc.softwarerenderer.vertex.Vertex;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PrimitiveAssembler {

    public static List<Triangle> assembleTriangles(final List<Vertex> vertices, @Nullable final ImageBuffer textureBuffer) {
        if (vertices.size() % 3 != 0) {
            throw new IllegalArgumentException("Vertices must be a multiple of 3");
        }
        final List<Triangle> triangles = new ArrayList<>(vertices.size() / 3);
        for (int i = 0; i < vertices.size(); i += 3) {
            final Vertex v1 = vertices.get(i);
            final Vertex v2 = vertices.get(i + 1);
            final Vertex v3 = vertices.get(i + 2);
            triangles.add(new Triangle(v1, v2, v3, textureBuffer));
        }
        return triangles;
    }

    public static List<Quad> assembleQuads(final List<Vertex> vertices, @Nullable final ImageBuffer textureBuffer) {
        if (vertices.size() % 4 != 0) {
            throw new IllegalArgumentException("Vertices must be a multiple of 4");
        }
        final List<Quad> quads = new ArrayList<>(vertices.size() / 4);
        for (int i = 0; i < vertices.size(); i += 4) {
            final Vertex v1 = vertices.get(i);
            final Vertex v2 = vertices.get(i + 1);
            final Vertex v3 = vertices.get(i + 2);
            final Vertex v4 = vertices.get(i + 3);
            quads.add(new Quad(v1, v2, v3, v4, textureBuffer));
        }
        return quads;
    }

}
