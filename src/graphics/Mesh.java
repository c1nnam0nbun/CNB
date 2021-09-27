package graphics;

import opengl.ShaderProgram;
import opengl.Vertex;
import opengl.VertexArrayObject;
import opengl.VertexBufferObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class Mesh implements Serializable {
    @Serial
    private static final long serialVersionUID = 654624578457865L;
    private float[] positions;
    private int[]   indices;
    private float[] normals;
    private float[] uvs;

    private VertexBufferObject v;
    private VertexBufferObject c;
    private VertexBufferObject i;
    private VertexBufferObject n;
    private VertexBufferObject uv;

    private VertexArrayObject vertexData;

    private boolean isPrepared = false;

    //region **********************************Constructors**********************************
    public Mesh(float[] positions, int[] indices, float[] normals, float[] uvs) {
        this.positions = positions;
        this.indices = indices;
        this.normals = normals;
        this.uvs = uvs;
        vertexData = new VertexArrayObject();
    }

    public Mesh(List<Vertex> vertices, int[] indices) {
        positions = new float[vertices.size() * 3];
        normals = new float[vertices.size() * 3];
        uvs = new float[vertices.size() * 2];
        this.indices = indices;

        for (int i = 0; i < vertices.size(); i++) {
            positions[i * 3] = vertices.get(i).position().x;
            positions[i * 3 + 1] = vertices.get(i).position().y;
            positions[i * 3 + 2] = vertices.get(i).position().z;

            normals[i * 3] = vertices.get(i).normal().x;
            normals[i * 3 + 1] = vertices.get(i).normal().y;
            normals[i * 3 + 2] = vertices.get(i).normal().z;
        }

        for (int i = 0; i < vertices.size(); i++) {
            uvs[i * 2] = vertices.get(i).uv().x;
            uvs[i * 2 + 1] = vertices.get(i).uv().y;
        }
//        System.out.println(indices.length);
//        System.out.println(Arrays.toString(indices));
        vertexData = new VertexArrayObject();
    }

    public Mesh() {
        this(null, null, null, null);
    }
    //endregion

    //region ************************************Methods*************************************
    private VertexBufferObject verticesToVBO() {
        if (positions == null) return null;
        //System.out.println(positions.length + "\n" + Arrays.toString(positions));
        return new VertexBufferObject(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW, 3);
    }

    private VertexBufferObject normalsToVBO(){
        if (normals == null) return null;
        return new VertexBufferObject(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW, 3);
    }

    private VertexBufferObject uvsToVBO() {
        if (uvs == null) return null;
        return new VertexBufferObject(GL_ARRAY_BUFFER, uvs, GL_STATIC_DRAW, 2);
    }

    private VertexBufferObject indicesToVBO() {
        if (indices == null) return null;
        return new VertexBufferObject(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }

    private void saveDataToVAO() {
        vertexData.bind();
        v = verticesToVBO();
        if (v != null) v.setAttributeName("position");
        n = normalsToVBO();
        if (n != null) n.setAttributeName("normal");
        uv = uvsToVBO();
        if (uv != null) uv.setAttributeName("uv");
        i = indicesToVBO();
        vertexData.store(v, n, i, uv, c);
    }

    public void prepare() {
        if (isPrepared) return;
        saveDataToVAO();
        vertexData.associate();
        isPrepared = true;
    }

    public void bind() {
        prepare();
        vertexData.bind();
    }

    public void unbind() {
        vertexData.unbind();
    }
    //endregion

    @Override
    public String toString() {
        return "Mesh{" +
                "positions=" + Arrays.toString(positions) +
                ", indices=" + Arrays.toString(indices) +
                ", normals=" + Arrays.toString(normals) +
                ", uvs=" + Arrays.toString(uvs) +
                ", v=" + v +
                ", c=" + c +
                ", i=" + i +
                ", n=" + n +
                ", uv=" + uv +
                ", vertexData=" + vertexData +
                ", isPrepared=" + isPrepared +
                '}';
    }


    //region ************************************Accessors***********************************

    public float[] getPositions() {
        return positions;
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public float[] getNormals() {
        return normals;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public float[] getUvs() {
        return uvs;
    }

    public void setUvs(float[] uvs) {
        this.uvs = uvs;
    }

    public VertexArrayObject getVertexData() {
        return vertexData;
    }

    //endregion
}
