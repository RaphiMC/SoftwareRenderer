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
package net.raphimc.softwarerenderer.swing;

import net.raphimc.softwarerenderer.SoftwareRenderer;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.function.BiFunction;

public abstract class SoftwareRendererCanvas<R extends SoftwareRenderer> extends Canvas implements Runnable {

    private final BiFunction<Integer, Integer, R> rendererSupplier;
    private BufferStrategy bufferStrategy;
    private R renderer;
    private Thread renderThread;
    protected volatile float frameTime;

    public SoftwareRendererCanvas(final BiFunction<Integer, Integer, R> rendererSupplier) {
        this.rendererSupplier = rendererSupplier;
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent event) {
                final int width = event.getComponent().getWidth();
                final int height = event.getComponent().getHeight();
                SoftwareRendererCanvas.this.renderer = SoftwareRendererCanvas.this.rendererSupplier.apply(width, height);
            }
        });
        this.addHierarchyListener(event -> {
            if ((event.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
                if (this.renderThread != null) {
                    this.renderThread.interrupt();
                    try {
                        this.renderThread.join(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    this.renderThread = null;
                }
                if (event.getComponent().isDisplayable()) {
                    this.createBufferStrategy(2);
                    this.bufferStrategy = this.getBufferStrategy();
                    this.renderThread = new Thread(this, this.getClass().getSimpleName() + "-RenderThread");
                    this.renderThread.setDaemon(true);
                    this.renderThread.start();
                }
            }
        });
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (this.renderer == null || this.bufferStrategy == null) {
                continue;
            }
            final long start = System.nanoTime();
            this.renderer.clearColorBuffer();
            this.renderer.clearDepthBuffer();
            this.render(this.renderer);
            final BufferedImage image = this.renderer.getImage();
            do {
                do {
                    final Graphics graphics = this.bufferStrategy.getDrawGraphics();
                    if (graphics == null) {
                        continue;
                    }
                    graphics.setColor(this.getClearColor());
                    graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                    graphics.setColor(Color.WHITE);
                    graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
                    graphics.dispose();
                } while (this.bufferStrategy.contentsRestored());
                this.bufferStrategy.show();
            } while (this.bufferStrategy.contentsLost());
            this.frameTime = (float) (System.nanoTime() - start) / 1_000_000F;
        }
    }

    protected abstract void render(final R renderer);

    protected Color getClearColor() {
        return Color.BLACK;
    }

}
