package ecs.components;

import core.MasterRenderer;
import ecs.Component;
import graphics.Material;

import static org.lwjgl.opengl.GL11.*;

public class RendererComponent extends Component {

    private Material material;

    public RendererComponent(Material material) {this.material = material; }

    public RendererComponent() { }

    @Override
    public void update() {
        if (material == null) this.material = new Material();
        render();
    }

    public void render() {
        material.getShader().start();
        material.apply();
        material.getShader().putMatrix(parent.transform().getTransformationMatrix(), "model");
        material.getShader().putMatrix(MasterRenderer.getActiveCamera().updateViewMatrix(), "view");
        material.getShader().putMatrix(MasterRenderer.getActiveCamera().createProjectionMatrix(), "projection");
        material.getShader().putVector(MasterRenderer.getActiveCamera().transform().getPosition(), "cameraPosition");
        glDrawElements(GL_TRIANGLES, parent.getComponent(MeshComponent.class).getMesh().getIndices().length, GL_UNSIGNED_INT, 0);
    }
}


