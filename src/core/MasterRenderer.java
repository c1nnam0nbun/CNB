package core;

import ecs.Camera;
import ecs.Entity;
import ecs.Light;
import ecs.components.MeshComponent;
import graphics.Mesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private static final Map<Mesh, List<Entity>> entities = new HashMap<>();
    private static final List<Camera> cameras = new ArrayList<>();
    private static final List<Light> lights = new ArrayList<>();

    private static Camera activeCamera;

    public static Light light;

    public static void update(Window window) {
        prepare();
        for (Mesh mesh : entities.keySet()) {
            if (mesh != null) mesh.bind();
            for (Entity entity : entities.get(mesh))
                entity.update();
            for (Light light : lights) {
                light.update();
            }
        }
        activeCamera.updateViewMatrix();
        swapBuffersAndPollEvents(window.getHandle());
    }


    public static void registerEntity(Entity entity) {
        Mesh mesh;
        if (entity.getComponent(MeshComponent.class) == null) mesh = null;
        else mesh = entity.getComponent(MeshComponent.class).getMesh();
        entities.computeIfAbsent(mesh, k -> new ArrayList<>());
        entities.get(mesh).add(entity);
    }

    public static void registerEntityList(List<Entity> entities) {
        for (Entity entity : entities) registerEntity(entity);
    }

    public static void registerLight(Light light) {
        if (!lights.contains(light)) lights.add(light);
    }

    public static void registerCamera(Camera camera) {
        if (!cameras.contains(camera)) cameras.add(camera);
        if (activeCamera == null) setActiveCamera(camera);
        camera.setId(cameras.indexOf(camera));
    }

    private static void prepare() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnableClientState(GL_VERTEX_ARRAY);
    }

    private static void swapBuffersAndPollEvents(long window) {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public static Camera getActiveCamera() {
        return activeCamera;
    }

    public static void setActiveCamera(Camera camera) {
        if (!cameras.contains(camera)) return;
        MasterRenderer.activeCamera = camera;
    }

    public static Camera getCameraById(int id) {
        return cameras.get(id);
    }
}
