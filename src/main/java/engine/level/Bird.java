package engine.level;

import engine.graphics.Shader;
import engine.graphics.Texture;
import engine.graphics.VertexArray;
import engine.input.Input;
import engine.math.Matrix4f;
import engine.math.Vector3f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class Bird {

    private float SIZE = 1.0f;
    private VertexArray mesh;
    private Texture texture;
    private float rotation;
    private float yDelta = 0.0f;

    private Vector3f position = new Vector3f();

    public Bird() {
        float[] vertices = new float[]{

                -SIZE / 2.0f, -SIZE / 2.0f, 0.1f,
                -SIZE / 2.0f, SIZE / 2.0f, 0.1f,
                SIZE / 2.0f, SIZE / 2.0f, 0.1f,
                SIZE / 2.0f, -SIZE / 2.0f, 0.1f
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

        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("textures/bird.png");
    }

    public void update() {
        position.y -= yDelta;
        if (Input.isKeyDown(GLFW_KEY_SPACE)) {
            yDelta = -0.15f;
        } else {
            yDelta +=0.01f;
        }
    }

    private void fall() {
        yDelta = -0.15f;
    }

    public void render() {
        Shader.BIRD.enable();
        Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        texture.bind();
        mesh.render();
        Shader.BIRD.disable();
    }


}
