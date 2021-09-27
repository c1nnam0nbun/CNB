package ecs;

import ecs.components.TransformComponent;
import org.joml.Matrix4f;
import util.Maths;

public class Camera {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = .1f;
    private static final float FAR_PLANE = 1000;
    private static int width;
    private static int height;
    private int id;

    private TransformComponent transform = new TransformComponent(Maths.vectorBack(), null, null);

    private Matrix4f projectionMatrix;

    public Camera(int width, int height) {
        Camera.width = width;
        Camera.height = height;
    }

    public Matrix4f createProjectionMatrix() {
        float aspectRatio = (float) width / (float) height;
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();

        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
        projectionMatrix.m23(-1f);
        projectionMatrix.m32(-((2f * NEAR_PLANE * FAR_PLANE) / frustumLength));
        projectionMatrix.m33(0);
        return projectionMatrix;
    }

    public Matrix4f updateViewMatrix() {
        Matrix4f viewMatrix = transform.getTransformationMatrix();
        viewMatrix.invert();
        return viewMatrix;
    }

    public TransformComponent transform() {
        return transform;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
