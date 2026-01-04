/*
 * This file is part of SoftwareRenderer - https://github.com/RaphiMC/SoftwareRenderer
 * Copyright (C) 2024-2026 RK_01/RaphiMC and contributors
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
package net.raphimc.softwarerenderer.rasterizer;

import net.raphimc.softwarerenderer.data.ClipRect;
import net.raphimc.softwarerenderer.data.ImageBuffer;
import net.raphimc.softwarerenderer.enums.CullFace;
import net.raphimc.softwarerenderer.vertex.RasterVertex;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public record QuadRasterizer(TriangleRasterizer tr1, TriangleRasterizer tr2, RasterVertex v1, RasterVertex v2, RasterVertex v3, RasterVertex v4) implements Rasterizer {

    public QuadRasterizer(final RasterVertex v1, final RasterVertex v2, final RasterVertex v3, final RasterVertex v4, @Nullable final ImageBuffer textureBuffer) {
        this(new TriangleRasterizer(v1, v2, v3, textureBuffer), new TriangleRasterizer(v3, v4, v1, textureBuffer), v1, v2, v3, v4);
    }

    @Override
    public void rasterize(final ImageBuffer colorBuffer, final float @Nullable [] depthBuffer, final @Nullable ClipRect clipRect) {
        this.tr1.rasterize(colorBuffer, depthBuffer, clipRect);
        this.tr2.rasterize(colorBuffer, depthBuffer, clipRect);
    }

    @Override
    public void drawWireframe(final Graphics graphics, @Nullable final ClipRect clipRect) {
        final int x1 = (int) this.v1.x();
        final int y1 = (int) this.v1.y();
        final int x2 = (int) this.v2.x();
        final int y2 = (int) this.v2.y();
        final int x3 = (int) this.v3.x();
        final int y3 = (int) this.v3.y();
        final int x4 = (int) this.v4.x();
        final int y4 = (int) this.v4.y();

        final Color previousColor = graphics.getColor();
        final Shape previousClip = graphics.getClip();
        graphics.setColor(new Color(this.v1.c(), true));
        if (clipRect != null) {
            graphics.setClip(clipRect.minX(), clipRect.minY(), clipRect.minX() + clipRect.maxX(), clipRect.minY() + clipRect.maxY());
        } else {
            graphics.setClip(null);
        }

        graphics.drawLine(x1, y1, x2, y2);
        graphics.drawLine(x2, y2, x3, y3);
        graphics.drawLine(x3, y3, x4, y4);
        graphics.drawLine(x4, y4, x1, y1);

        graphics.setColor(previousColor);
        graphics.setClip(previousClip);
    }

    @Override
    public boolean canBeCulled(final CullFace cullFace) {
        return this.tr1.canBeCulled(cullFace) && this.tr2.canBeCulled(cullFace);
    }

}
