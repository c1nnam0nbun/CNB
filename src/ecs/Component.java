package ecs;

public abstract class Component {

    protected Entity parent;

    public Component() {}

    public Entity getParent() {
        return parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public abstract void update();
}
