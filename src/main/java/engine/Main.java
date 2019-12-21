package engine;

import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Input;
import engine.io.Window;
import engine.maths.Vector2f;
import engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.logging.Logger;

public class Main implements Runnable {

    private Window window;
    private Renderer renderer;
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private Shader shader;
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private Mesh mesh = new Mesh(new Vertex[]{
            new Vertex(
                    /* <--coordinates of a single vertex |  3f vector for colors of the vertices--> */
                    new Vector3f(-0.5f, 0.5f, 0.0f),
                    new Vector3f(0.5f, 0.0f, 0.0f),
                    new Vector2f(0f, 0f)),
            new Vertex(
                    new Vector3f(-0.5f, -0.5f, 0.0f),
                    new Vector3f(0.0f, 0.5f, 0.0f),
                    new Vector2f(0, 1)
            ),
            new Vertex(
                    new Vector3f(0.5f, -0.5f, 0.0f),
                    new Vector3f(0.0f, 0.0f, 0.5f),
                    new Vector2f(1, 1)
            ),
            new Vertex(
                    new Vector3f(0.5f, 0.5f, 0.0f),
                    new Vector3f(0.0f, 0.5f, 0.5f),
                    new Vector2f(1, 0))
    }, new int[]{
            0, 1, 2
            , 0, 3, 2
    },
            new Material("/textures/beautiful.png"));


    public void start() {
        Thread game = new Thread(this, "game");
        game.start();
    }

    public void init() {
        window = new Window(WIDTH, HEIGHT, "Game");
        shader = new Shader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl");
        renderer = new Renderer(shader);
        window.setBackgroundColor(1.0f, 0, 0);
        window.create();
        mesh.create();
        shader.create();
    }

    public void run() {
        init();
        while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            update();
            render();
            if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) {
                window.setFullscreen(!window.isFullscreen());
            }
        }
        close();
    }

    private void render() {
        renderer.renderMesh(mesh);
        window.swapBuffers();
    }

    private void update() {
        window.update();
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            LOGGER.info("X: " + Input.getMouseX() + ", Y: " + Input.getMouseY());
        }
    }

    private void close() {
        window.destroy();
        mesh.destroy();
        shader.destroy();
    }

    public static void main(final String[] args) {
        new Main().start();
    }
}
