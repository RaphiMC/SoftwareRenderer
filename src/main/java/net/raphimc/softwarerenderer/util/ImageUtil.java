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

import java.awt.image.BufferedImage;

public class ImageUtil {

    public static BufferedImage ensureArgb(final BufferedImage image) {
        if (image.getType() != BufferedImage.TYPE_INT_ARGB && image.getType() != BufferedImage.TYPE_INT_ARGB_PRE) {
            final BufferedImage newTexture = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            newTexture.getGraphics().drawImage(image, 0, 0, null);
            return newTexture;
        } else {
            return image;
        }
    }

}
