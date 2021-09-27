package opengl;

public record Uniform(String name, int size, int type, int location) {

    @Override
    public String toString() {
        return "Uniform{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", type=" + type +
                ", location=" + location +
                '}';
    }
}
