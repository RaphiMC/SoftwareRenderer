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

public class ColorMixer {

    public static int mix(final int c1, final int c2) {
        final int a1 = (c1 >> 24) & 0xFF;
        final int r1 = (c1 >> 16) & 0xFF;
        final int g1 = (c1 >> 8) & 0xFF;
        final int b1 = c1 & 0xFF;
        final int a2 = (c2 >> 24) & 0xFF;
        final int r2 = (c2 >> 16) & 0xFF;
        final int g2 = (c2 >> 8) & 0xFF;
        final int b2 = c2 & 0xFF;

        final int a = (int) (a1 * a2 / 255F);
        final int r = (int) (r1 * r2 / 255F);
        final int g = (int) (g1 * g2 / 255F);
        final int b = (int) (b1 * b2 / 255F);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int blend(final int dstColor, final int srcColor) {
        final int dstR = (dstColor >> 16) & 0xFF;
        final int dstG = (dstColor >> 8) & 0xFF;
        final int dstB = dstColor & 0xFF;
        final int srcA = (srcColor >> 24) & 0xFF;
        final int srcR = (srcColor >> 16) & 0xFF;
        final int srcG = (srcColor >> 8) & 0xFF;
        final int srcB = srcColor & 0xFF;

        final float srcAlpha = srcA / 255F;
        final float invSrcAlpha = 1F - srcAlpha;

        final int outR = (int) (srcR * srcAlpha + dstR * invSrcAlpha);
        final int outG = (int) (srcG * srcAlpha + dstG * invSrcAlpha);
        final int outB = (int) (srcB * srcAlpha + dstB * invSrcAlpha);

        return (0xFF << 24) | (outR << 16) | (outG << 8) | outB;
    }

}
