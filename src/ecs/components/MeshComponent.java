package ecs.components;

import ecs.Component;
import ecs.Entity;
import graphics.Mesh;

public class MeshComponent extends Component {

    private Mesh mesh;

    public MeshComponent(Mesh mesh) {
        this.mesh = mesh;
    }

    public MeshComponent() {
    }

    @Override
    public void update() {}

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}
