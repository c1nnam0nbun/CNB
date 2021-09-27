package ecs;

import ecs.components.TransformComponent;
import opengl.ShaderProgram;
import org.joml.Vector3f;

public class Light {

    private TransformComponent transform;
    private Vector3f color;
//    private Vector3f attenuation = new Vector3f(1, 0.002f, 0.0002f);
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    public Light(TransformComponent transform, Vector3f color) {
        this.transform = transform;
        this.color = color;
    }

    public void update() {
        ShaderProgram.getActiveShader().putVector(transform.getPosition(), "lightPosition");
        ShaderProgram.getActiveShader().putVector(color, "lightColor");
        ShaderProgram.getActiveShader().putVector(attenuation, "attenuation");
    }

    public TransformComponent transform() {
        return transform;
    }

    public Vector3f getColor() {
        return color;
    }
}
