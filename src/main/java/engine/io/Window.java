package engine.io;

import engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.io.PrintStream;
import java.rmi.server.LogStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private int width;
    private int height;
    private String title;
    private long windowHandle;
    private int frames;
    private long time;
    private Input input;
    private Vector3f background = new Vector3f(0, 0, 0);
    private GLFWWindowSizeCallback sizeCallback;
    private boolean isResized;
    private boolean isFullscreen;
    private int[] windowPosX = new int[1];
    private int[] windowPosY = new int[1];

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }


    public void create() {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        if (!GLFW.glfwInit()) {
            System.err.println("ERROR: GLFW wasn't initialized");
            return;
        }

        input = new Input();
        System.out.println(glfwGetPrimaryMonitor());

        glfwWindowHint(GLFW_RESIZABLE, GL11.GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        windowHandle = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);

        if (windowHandle == 0) {
            System.err.println("ERROR: Window wasn't created");
            return;
        }

//        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
//        windowPosX[0] = (Objects.requireNonNull(videoMode).width() - width) / 2;
//        windowPosY[0] = (videoMode.height() - height) / 2;
//        GLFW.glfwSetWindowPos(windowHandle, windowPosX[0], windowPosY[0]);
//        GLFW.glfwMakeContextCurrent(windowHandle);
//        GL.createCapabilities();
//        GL11.glEnable(GL11.GL_DEPTH_TEST);

        System.out.println(windowPosX[0]);
        System.out.println(windowPosX[0]);

        createCallbacks();

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);
        windowPosX[0] = (Objects.requireNonNull(videoMode).width() - width) / 2;
        windowPosY[0] = (videoMode.height() - height) / 2;
        GLFW.glfwSetWindowPos(windowHandle, windowPosX[0], windowPosY[0]);
        GLFW.glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GLFW.glfwShowWindow(windowHandle);



        int[] monitorPosX = new int[10];
        int[] monitorPosY = new int[10];
        GLFW.glfwGetMonitorPos(monitor, monitorPosX, monitorPosY);

        int[] PosXwin = new int[10];
        int[] PosYwin = new int[10];
        GLFW.glfwGetWindowPos(windowHandle, PosXwin, PosYwin);

        GLFW.glfwSwapInterval(1);

        time = System.currentTimeMillis();
    }

    private void createCallbacks() {
        sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true;
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(windowHandle, input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(windowHandle, input.getMouseButtonsCallback());
        GLFW.glfwSetScrollCallback(windowHandle, input.getMouseScrollCallback());
        GLFW.glfwSetWindowSizeCallback(windowHandle, sizeCallback);
    }

    public void update() {
        if (isResized) {
            GL11.glViewport(0, 0, width, height);
            isResized = false;
        }
        GL11.glClearColor(background.getX(), background.getY(), background.getZ(), 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();
        frames++;
        if (System.currentTimeMillis() > time + 1000) {
            GLFW.glfwSetWindowTitle(windowHandle, title + " | FPS: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(windowHandle);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void destroy() {
        input.destroy();
        sizeCallback.free();
        GLFW.glfwWindowShouldClose(windowHandle);
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
    }

    public void setBackgroundColor(float r, float g, float b) {
        background.set(r, g, b);
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        isResized = true;
        if (isFullscreen) {
            GLFW.glfwGetWindowPos(windowHandle, windowPosX, windowPosY);
            GLFW.glfwSetWindowMonitor(windowHandle, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
        } else {
            GLFW.glfwSetWindowMonitor(windowHandle, 0, windowPosX[0], windowPosY[0], width, height, 0);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public long getWindowHandle() {
        return windowHandle;
    }
}
