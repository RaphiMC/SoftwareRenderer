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
import net.raphimc.softwarerenderer.util.ColorMixer;
import net.raphimc.softwarerenderer.vertex.RasterVertex;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public record TriangleRasterizer(RasterVertex v1, RasterVertex v2, RasterVertex v3, @Nullable ImageBuffer textureBuffer) implements Rasterizer {

    @Override
    public void rasterize(final ImageBuffer colorBuffer, final float @Nullable [] depthBuffer, @Nullable final ClipRect clipRect) {
        final float x1 = this.v1.x();
        final float y1 = this.v1.y();
        final float z1 = this.v1.z();
        final float w1 = this.v1.w();
        final float x2 = this.v2.x();
        final float y2 = this.v2.y();
        final float z2 = this.v2.z();
        final float w2 = this.v2.w();
        final float x3 = this.v3.x();
        final float y3 = this.v3.y();
        final float z3 = this.v3.z();
        final float w3 = this.v3.w();
        final int rasterWidth = colorBuffer.width();
        final int rasterHeight = colorBuffer.height();
        final int[] colorRaster = colorBuffer.raster();
        final boolean isBackFacing = this.isBackFacing();

        int minX = (int) Math.floor(Math.max(0, Math.min(Math.min(x1, x2), Math.min(x3, rasterWidth - 1))));
        int minY = (int) Math.floor(Math.max(0, Math.min(Math.min(y1, y2), Math.min(y3, rasterHeight - 1))));
        int maxX = (int) Math.ceil(Math.min(rasterWidth - 1, Math.max(Math.max(x1, x2), Math.max(x3, 0))));
        int maxY = (int) Math.ceil(Math.min(rasterHeight - 1, Math.max(Math.max(y1, y2), Math.max(y3, 0))));
        if (clipRect != null) {
            minX = Math.max(minX, clipRect.minX());
            minY = Math.max(minY, clipRect.minY());
            maxX = Math.min(maxX, clipRect.maxX());
            maxY = Math.min(maxY, clipRect.maxY());
        }

        final float dx1 = x3 - x2;
        final float dy1 = y3 - y2;
        final float dx2 = x1 - x3;
        final float dy2 = y1 - y3;
        final float dx3 = x2 - x1;
        final float dy3 = y2 - y1;

        final int a1 = (this.v1.c() >> 24) & 0xFF;
        final int r1 = (this.v1.c() >> 16) & 0xFF;
        final int g1 = (this.v1.c() >> 8) & 0xFF;
        final int b1 = this.v1.c() & 0xFF;
        final int a2 = (this.v2.c() >> 24) & 0xFF;
        final int r2 = (this.v2.c() >> 16) & 0xFF;
        final int g2 = (this.v2.c() >> 8) & 0xFF;
        final int b2 = this.v2.c() & 0xFF;
        final int a3 = (this.v3.c() >> 24) & 0xFF;
        final int r3 = (this.v3.c() >> 16) & 0xFF;
        final int g3 = (this.v3.c() >> 8) & 0xFF;
        final int b3 = this.v3.c() & 0xFF;

        final int[] textureRaster;
        final int textureWidth;
        final int textureHeight;
        final float u1;
        final float v1;
        final float u2;
        final float v2;
        final float u3;
        final float v3;
        if (this.textureBuffer != null) {
            textureRaster = this.textureBuffer.raster();
            textureWidth = this.textureBuffer.width();
            textureHeight = this.textureBuffer.height();
            u1 = this.v1.u() / w1 * textureWidth;
            v1 = this.v1.v() / w1 * textureHeight;
            u2 = this.v2.u() / w2 * textureWidth;
            v2 = this.v2.v() / w2 * textureHeight;
            u3 = this.v3.u() / w3 * textureWidth;
            v3 = this.v3.v() / w3 * textureHeight;
        } else {
            textureRaster = null;
            textureWidth = textureHeight = 0;
            u1 = v1 = u2 = v2 = u3 = v3 = 0;
        }

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                final double bc1 = (x - x2) * dy1 - (y - y2) * dx1;
                final double bc2 = (x - x3) * dy2 - (y - y3) * dx2;
                final double bc3 = (x - x1) * dy3 - (y - y1) * dx3;
                if (isBackFacing ? (bc1 >= 0 && bc2 >= 0 && bc3 >= 0) : (bc1 <= 0 && bc2 <= 0 && bc3 <= 0)) {
                    final double sum = bc1 + bc2 + bc3;
                    if (sum == 0) {
                        continue;
                    }
                    final int rasterIndex = y * rasterWidth + x;

                    if (depthBuffer != null) {
                        final float z = (float) ((bc1 * z1 + bc2 * z2 + bc3 * z3) / sum);
                        if (z >= depthBuffer[rasterIndex]) {
                            continue;
                        }
                        depthBuffer[rasterIndex] = z;
                    }

                    final double pSum = (bc1 / w1) + (bc2 / w2) + (bc3 / w3);

                    final int a = (int) ((bc1 * a1 + bc2 * a2 + bc3 * a3) / sum);
                    final int r = (int) ((bc1 * r1 + bc2 * r2 + bc3 * r3) / sum);
                    final int g = (int) ((bc1 * g1 + bc2 * g2 + bc3 * g3) / sum);
                    final int b = (int) ((bc1 * b1 + bc2 * b2 + bc3 * b3) / sum);
                    int color = (a << 24) | (r << 16) | (g << 8) | b;

                    if (textureRaster != null) {
                        final double u = (bc1 * u1 + bc2 * u2 + bc3 * u3) / pSum;
                        final double v = (bc1 * v1 + bc2 * v2 + bc3 * v3) / pSum;

                        final int texX = (int) Math.min(Math.max(u, 0), textureWidth - 1);
                        final int texY = (int) Math.min(Math.max(v, 0), textureHeight - 1);
                        final int texColor = textureRaster[texY * textureWidth + texX];
                        color = ColorMixer.mix(color, texColor);
                    }

                    if ((color & 0xFF000000) != 0) {
                        colorRaster[rasterIndex] = ColorMixer.blend(colorRaster[rasterIndex], color);
                    }
                }
            }
        }
    }

    @Override
    public void drawWireframe(final Graphics graphics, @Nullable final ClipRect clipRect) {
        final int x1 = (int) this.v1.x();
        final int y1 = (int) this.v1.y();
        final int x2 = (int) this.v2.x();
        final int y2 = (int) this.v2.y();
        final int x3 = (int) this.v3.x();
        final int y3 = (int) this.v3.y();

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
        graphics.drawLine(x3, y3, x1, y1);

        graphics.setColor(previousColor);
        graphics.setClip(previousClip);
    }

    @Override
    public boolean canBeCulled(final CullFace cullFace) {
        return switch (cullFace) {
            case NONE -> false;
            case BACK -> this.isFrontFacing();
            case FRONT -> this.isBackFacing();
        };
    }

    public boolean isBackFacing() {
        return (this.v2.x() - this.v1.x()) * (this.v3.y() - this.v1.y()) - (this.v2.y() - this.v1.y()) * (this.v3.x() - this.v1.x()) < 0;
    }

    public boolean isFrontFacing() {
        return (this.v2.x() - this.v1.x()) * (this.v3.y() - this.v1.y()) - (this.v2.y() - this.v1.y()) * (this.v3.x() - this.v1.x()) > 0;
    }

}
