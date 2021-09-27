package ecs;

import ecs.components.MeshComponent;
import ecs.components.TransformComponent;
import graphics.Mesh;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Entity {

    TransformComponent transform;
    List<Component> components = new ArrayList<>();

    public Entity(TransformComponent transform) {
        this.transform = transform;
    }

    public Entity() {
        this(new TransformComponent());
    }

    public void addComponent(Component component) {
        components.add(component);
        component.setParent(this);
    }

    public TransformComponent transform() {
        return transform;
    }

    public <U extends Component> U getComponent(Class<U> clazz) {
        for (Component component : components) {
            if (clazz.isInstance(component)) {
                return clazz.cast(component);
            }
        }
        return null;
    }

    public void update() {
        for (Component c : components) {
            c.update();
        }
    }
}
