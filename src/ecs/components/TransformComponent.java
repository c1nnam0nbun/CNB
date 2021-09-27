package ecs.components;

import ecs.Component;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import util.Maths;

import static util.Maths.*;

public class TransformComponent extends Component {

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    private Matrix4f transformationMatrix;

    public TransformComponent(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position == null ? Maths.vectorZero() : position;
        this.rotation = rotation == null ? Maths.vectorZero() : rotation;
        this.scale = scale == null ? Maths.vectorOne() : scale;
    }

    public TransformComponent() {
        this(null, null, null);
    }

    @Override
    public void update() {}

    public TransformComponent move(Vector3f velocity) {
        position.add(velocity);
        return this;
    }

    public TransformComponent rotate(Vector3f velocity) {
        rotation.add(velocity);
        return this;
    }

    public TransformComponent scale(Vector3f amount) {
        scale.add(amount);
        if (scale.x <= 0 ) scale.x = 0;
        if (scale.y <= 0 ) scale.y = 0;
        if (scale.z <= 0 ) scale.z = 0;
        return this;
    }

    public Matrix4f getTransformationMatrix() {
        transformationMatrix = new Matrix4f();
        transformationMatrix.identity();
        transformationMatrix.translate(position, transformationMatrix);
        transformationMatrix.rotate((float) Math.toRadians(rotation.x), vectorRight());
        transformationMatrix.rotate((float) Math.toRadians(rotation.y), vectorUp());
        transformationMatrix.rotate((float) Math.toRadians(rotation.z), vectorForward());
        transformationMatrix.scale(scale);
        return transformationMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }
}
