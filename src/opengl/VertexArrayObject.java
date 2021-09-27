package opengl;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 8743464567483468L;

    private final int id;
    private final List<VertexBufferObject> vbos = new ArrayList<>();

    private static VertexArrayObject activeVAO;

    public VertexArrayObject() {
        id = glGenVertexArrays();
    }

    public void store(VertexBufferObject vbo) {
        if (vbo != null) vbos.add(vbo);
    }

    public void store(VertexBufferObject... vbos) {
        for (VertexBufferObject vbo : vbos) store(vbo);
    }

    public void associate(){
        bind();
        for (VertexBufferObject vbo : vbos) {
            if (vbo.getAttributeName() == null) continue;
            for (Attribute attr : ShaderProgram.getActiveShader().getAttributes()) {
                if (vbo.getAttributeName().equals(attr.name())) {
                    System.out.println(vbo.getAttributeName() + " " + attr.location() + " " + vbo.getDataType());

                    vbo.bind();
                    glVertexAttribPointer(attr.location(), vbo.getCoordSize(), vbo.getDataType(), false, 0, 0);
                    glEnableVertexAttribArray(attr.location());
                    vbo.unbind();

                    break;
                }
            }
        }
        unbind();
    }


    public void bind() {
        glBindVertexArray(id);
        VertexArrayObject.setActiveVAO(this);
    }

    public void unbind() {
        glBindVertexArray(0);
        VertexArrayObject.setActiveVAO(null);
    }

    public static VertexArrayObject getActiveVAO() {
        return activeVAO;
    }

    public static void setActiveVAO(VertexArrayObject activeVAO) {
        VertexArrayObject.activeVAO = activeVAO;
    }

    public int getId() {
        return id;
    }
}
