package engine.level;

import engine.graphics.Shader;
import engine.graphics.Texture;
import engine.graphics.VertexArray;
import engine.math.Matrix4f;
import engine.math.Vector3f;

public class Level {

    private final VertexArray background;

    private final Texture bgTexture;

    private int xScroll = 0;
    private int map = 0;

    private Bird bird;

    public Level() {
        float[] vertices = new float[]{

                -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
                -10.0f, 10.0f * 9.0f / 16.0f, 0.0f,
                0.0f, 10.0f * 9.0f / 16.0f, 0.0f,
                0.0f, -10.0f * 9.0f / 16.0f, 0.0f
        };

        byte[] indices = new byte[]{
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[]{
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        background = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("textures/bg.jpeg");

        bird = new Bird();
    }

    public void update() {
        xScroll--;
        if(-xScroll % 335 == 0) {
            map++;
        }
        bird.update();
    }

    public void render() {
        bgTexture.bind();
        Shader.BG.enable();
        background.bind();
        for (int i = map; i < map + 4; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
            background.draw();
        }
        Shader.BG.disable();
        bgTexture.unbind();

        bird.render();
    }
}
