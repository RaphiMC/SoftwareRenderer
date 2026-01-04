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
package net.raphimc.softwarerenderer.data;

import net.raphimc.softwarerenderer.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public record ImageBuffer(int[] raster, int width, int height) {

    public ImageBuffer(final BufferedImage image) {
        this(((DataBufferInt) ImageUtil.ensureArgb(image).getRaster().getDataBuffer()).getData(), image.getWidth(), image.getHeight());
    }

}
