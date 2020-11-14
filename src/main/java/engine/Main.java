package engine;


import engine.graphics.Shader;
import engine.input.Input;
import engine.level.Level;
import engine.math.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main implements Runnable{

    private int width = 1280;
    private int height = 720;

    private Thread thread;
    private boolean running = false;

    private long window;

    private Level level;

    public void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    private void init() {
        if (!glfwInit()) {
            //TODO: handle it
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        window = glfwCreateWindow(width, height, "Game", NULL, NULL);

        if(window == NULL) {
            //TOOD: handle
            return;
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        glfwSetKeyCallback(window, new Input());

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities(); //call this before calling GL methods

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));
        Shader.loadAll();

        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);

        level = new Level();
    }

    public void run() {
        init();

        long lastTime = System.nanoTime();
        double delta = 0.0;
        double ns = 1000000000.0 / 60.0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        while(running) {
            long now = System.nanoTime();
            delta+= (now - lastTime) / ns;
            lastTime = now;
            if(delta>=1.0) {
                update();
                updates++;
                delta--;
            }

            render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                timer+=1000;
                System.out.println(updates + "ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
            if(glfwWindowShouldClose(window)) {
                running = false;
            }
        }
    }

    private void update() {
        glfwPollEvents();
        level.update();
        if(Input.keys[GLFW_KEY_SPACE]) {
            System.out.println("FLAP!");
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();

        int err = glGetError();
        if( err != GL_NO_ERROR) {
            System.out.println(err);
        }
        glfwSwapBuffers(window);

    }

    public static void main(String[] args) {
        new Main().start();

    }
}