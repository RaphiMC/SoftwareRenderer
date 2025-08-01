import net.raphimc.softwarerenderer.PerspectiveSoftwareRenderer;
import net.raphimc.softwarerenderer.data.ImageBuffer;
import net.raphimc.softwarerenderer.enums.CullFace;
import net.raphimc.softwarerenderer.primitives.Quad;
import net.raphimc.softwarerenderer.swing.SoftwareRendererCanvas;
import net.raphimc.softwarerenderer.vertex.FloatVertex;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExampleScene extends SoftwareRendererCanvas<PerspectiveSoftwareRenderer> {

    private final ImageBuffer textureBuffer;

    public ExampleScene() {
        super((width, height) -> new PerspectiveSoftwareRenderer(width, height, 90F));
        try {
            this.textureBuffer = new ImageBuffer(ImageIO.read(this.getClass().getResourceAsStream("stone.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void render(final PerspectiveSoftwareRenderer renderer) {
        final List<Quad> quads = new ArrayList<>();
        box(quads, new Vector3f(-1, -3, -1), new Vector3f(1, -1, 1), this.textureBuffer);
        box(quads, new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1), this.textureBuffer);
        box(quads, new Vector3f(1, -1, -1), new Vector3f(3, 1, 1), this.textureBuffer);
        box(quads, new Vector3f(-3, -1, -1), new Vector3f(-1, 1, 1), this.textureBuffer);
        box(quads, new Vector3f(-1, 1, -1), new Vector3f(1, 3, 1), this.textureBuffer);

        renderer.setDepthEnabled(true);
        renderer.setCullFace(CullFace.BACK);
        renderer.getModelViewMatrix().pushMatrix();

        renderer.getModelViewMatrix().translate(0, 0, -5);
        renderer.getModelViewMatrix().rotateX((float) Math.toRadians((System.currentTimeMillis() % 5000) / 5000F * 360));
        renderer.getModelViewMatrix().rotateZ((float) Math.toRadians((System.currentTimeMillis() % 5000) / 5000F * 360));

        final int renderedQuads = renderer.draw3DPrimitives(quads);

        renderer.getModelViewMatrix().popMatrix();
        renderer.setCullFace(CullFace.NONE);
        renderer.setDepthEnabled(false);

        final float fps = 1000F / this.frameTime;
        renderer.getGraphics2D().drawString("FPS: " + fps, 10, 20);
        renderer.getGraphics2D().drawString("Frame time: " + this.frameTime + "ms", 10, 40);
        renderer.getGraphics2D().drawString("Quads: " + renderedQuads, 10, 60);
    }

    public static void box(final List<Quad> quads, final Vector3f pos1, final Vector3f pos2, final ImageBuffer texture) {
        final float minX = pos1.x;
        final float minY = pos1.y;
        final float minZ = pos1.z;
        final float maxX = pos2.x;
        final float maxY = pos2.y;
        final float maxZ = pos2.z;
        texturedRectangle(quads, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, texture);
        texturedRectangle(quads, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, minX, maxY, maxZ, texture);
        texturedRectangle(quads, maxX, maxY, minZ, maxX, minY, minZ, minX, minY, minZ, minX, maxY, minZ, texture);
        texturedRectangle(quads, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, texture);
        texturedRectangle(quads, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, texture);
        texturedRectangle(quads, maxX, maxY, maxZ, maxX, minY, maxZ, maxX, minY, minZ, maxX, maxY, minZ, texture);
    }

    public static void coloredRectangle(final List<Quad> quads, final float xbl, final float ybl, final float zbl, final float xbr, final float ybr, final float zbr, final float xtr, final float ytr, final float ztr, final float xtl, final float ytl, final float ztl, final int c) {
        quads.add(new Quad(
                new FloatVertex(xbl, ybl, zbl, c),
                new FloatVertex(xbr, ybr, zbr, c),
                new FloatVertex(xtr, ytr, ztr, c),
                new FloatVertex(xtl, ytl, ztl, c),
                null
        ));
    }

    public static void texturedRectangle(final List<Quad> quads, final float xbl, final float ybl, final float zbl, final float xbr, final float ybr, final float zbr, final float xtr, final float ytr, final float ztr, final float xtl, final float ytl, final float ztl, final ImageBuffer texture) {
        quads.add(new Quad(
                new FloatVertex(xbl, ybl, zbl, 0xFFFFFFFF, 0F, 1F),
                new FloatVertex(xbr, ybr, zbr, 0xFFFFFFFF, 1F, 1F),
                new FloatVertex(xtr, ytr, ztr, 0xFFFFFFFF, 1F, 0F),
                new FloatVertex(xtl, ytl, ztl, 0xFFFFFFFF, 0F, 0F),
                texture
        ));
    }

}
