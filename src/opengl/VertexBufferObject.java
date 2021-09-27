package opengl;

import java.io.Serial;
import java.io.Serializable;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.BufferUtils.*;
import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 35267598430897654L;

    private final int id;
    private int dataType;
    private int target;
    private int usage;
    private long size;
    private int coordSize;

    private String attributeName;

    private static final int SIZE_FACTOR = 3;

    public VertexBufferObject(int target, int usage, int dataType, long size, int coordSize) {
        this();
        this.target = target;
        this.dataType = dataType;
        this.usage = usage;
        this.coordSize = coordSize;
        this.size = size;
    }

    public VertexBufferObject(int target, float[] data, int usage, int coordSize) {
        this(target, usage, GL_FLOAT, (long) data.length * SIZE_FACTOR * Float.BYTES, coordSize);

        bind();
        uploadData(data);
    }

    public VertexBufferObject(int target, long size, int usage) {
        this(target, usage, GL_FLOAT, size, 0);

        bind();
        uploadData(size);
    }

    public VertexBufferObject(int target, int[] data, int usage) {
        this(target, usage, GL_FLOAT, (long) data.length * SIZE_FACTOR * Float.BYTES, 0);

        bind();
        uploadData(data);
    }

    public VertexBufferObject(int target, int[] data, int usage, int coordSize) {
        this(target, usage, GL_FLOAT, (long) data.length * SIZE_FACTOR * Float.BYTES, coordSize);

        bind();
        uploadData(data);
    }

    public VertexBufferObject() {
        id = glGenBuffers();
    }

    public void uploadData(float[] data) {
        glBufferData(target, arrayToBuffer(data), usage);
    }

    public void uploadData(int[] data) {
        glBufferData(target, arrayToBuffer(data), usage);
    }

    public void uploadData(long size) {
        glBufferData(target, size, usage);
    }

    public void uploadSubData(long offset, float[] data) {
        glBufferSubData(target, offset, arrayToBuffer(data));
    }

    public void uploadSubData(long offset, int[] data) {
        glBufferSubData(target, offset, arrayToBuffer(data));
    }

    public void uploadSubData(long offset, byte[] data) {
        glBufferSubData(target, offset, arrayToBuffer(data));
    }

    private FloatBuffer arrayToBuffer(float[] data) {
        FloatBuffer buffer = createFloatBuffer((int) size);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private IntBuffer arrayToBuffer(int[] data) {
        IntBuffer buffer = createIntBuffer((int) size);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private ByteBuffer arrayToBuffer(byte[] data) {
        ByteBuffer buffer = createByteBuffer((int) size);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void bind() {
        glBindBuffer(target, id);
    }

    public void unbind() {
        glBindBuffer(target, 0);
    }

    public void delete() {
        glDeleteBuffers(id);
    }

    //region **********************************Accessors**********************************
    public int getDataType() {
        return dataType;
    }

    public int getTarget() {
        return target;
    }

    public int getUsage() {
        return usage;
    }

    public long getSize() {
        return size;
    }

    public int getCoordSize() {
        return coordSize;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    //endregion
}
