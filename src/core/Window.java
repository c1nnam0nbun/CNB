package core;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Window {
    public static final long NULL = MemoryUtil.NULL;
    private long handle;

    private int width, height;
    private String title;
    private long monitor;
    private long share;

    public Window(int width, int height, String title, long monitor, long share) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.monitor = monitor;
        this.share = share;
        init(width, height, title, monitor, share);
    }

    private void init(int width, int height, String title, long monitor, long share) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        handle = glfwCreateWindow(width, height, title, monitor, share);
        if ( handle == NULL ) throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(handle, Input::onKeyboardInputReceived);
        glfwSetMouseButtonCallback(handle,  Input::onMouseInputReceived);
        glfwSetScrollCallback(handle,  Input::onMouseWheelScrollReceived);


        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(handle, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    handle,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(handle);
        createCapabilities();
        glfwSwapInterval(1);

        glfwShowWindow(handle);
    }

    public void cleanUp() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);

        glDisableClientState(GL_VERTEX_ARRAY);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public long getHandle() {
        return handle;
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

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(handle, title);
    }

    public long getMonitor() {
        return monitor;
    }

    public long getShare() {
        return share;
    }
}
