package opengl;

public record Attribute(String name, int size, int type, int location) {

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", type=" + type +
                ", location=" + location +
                '}';
    }
}
