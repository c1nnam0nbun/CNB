package opengl;

import org.joml.Vector2f;
import org.joml.Vector3f;

public record Vertex(Vector3f position, Vector2f uv, Vector3f normal) {

    @Override
    public Vector3f position() {
        return position;
    }

    @Override
    public Vector2f uv() {
        return uv;
    }

    @Override
    public Vector3f normal() {
        return normal;
    }

    public boolean equals(Vertex v) {
        return position.equals(v.position.x, v.position.y, v.position.z) &&
               uv.equals(v.uv.x, v.uv.y) &&
               normal.equals(v.normal.x, v.normal.y, v.normal.z);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "position=" + position +
                ", uv=" + uv +
                ", normal=" + normal +
                "}\n";
    }

    public float[] toArray() {
        return new float[] {position.x, position.y, position.x, uv.x, uv.y, normal.x, normal.y, normal.z};
    }
}
