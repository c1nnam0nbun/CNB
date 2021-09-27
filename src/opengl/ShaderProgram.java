package opengl;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.BufferUtils.createIntBuffer;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
    private final int vertexShader;
    private final int fragmentShader;
    private final int program;

    private final List<Attribute> attributes = new ArrayList<>();
    private final List<Uniform> uniforms = new ArrayList<>();

    private static ShaderProgram activeShader;
    private static ShaderProgram defaultShader;

    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
        String vertexShaderSource = loadShader(vertexShaderPath);
        String fragmentShaderSource = loadShader(fragmentShaderPath);
        vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderSource);
        fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderSource);
        program = createProgram();
        attachShaders();
        link();

        start();
        getShaderAttributes();
        getShaderUniforms();
        stop();

    }

    public static void initDefaultShader() {
        String vertexSourcePath = "src/shaders/BaseVertexShader.shader";
        String fragmentSourcePath = "src/shaders/BaseFragmentShader.shader";

        defaultShader = new ShaderProgram(vertexSourcePath, fragmentSourcePath);
    }

    private String loadShader(String path) {
        Path source = Paths.get(path);
        StringBuilder shader = new StringBuilder();
        try {
            for (String line : Files.readAllLines(source)) shader.append(line).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shader.toString();
    }

    private int compileShader(int type, String source) {
        int shader = glCreateShader(type);
        if (shader == 0) return 0;
        glShaderSource(shader, source);
        glCompileShader(shader);

        String err = glGetShaderInfoLog(shader);
        if (!err.equals("")) {
            System.out.println(err);
        }

        return shader;
    }

    private void getShaderAttributes() {
        int count = glGetProgrami(program, GL_ACTIVE_ATTRIBUTES);
        IntBuffer size = createIntBuffer(1);
        IntBuffer type = createIntBuffer(1);
        for (int i = 0; i < count; i++) {
            String name = glGetActiveAttrib(program, i, size, type);
            int location = glGetAttribLocation(program, name);
            attributes.add(new Attribute(name, size.get(0), type.get(0), location));
        }
    }

    private void getShaderUniforms() {
        int count = glGetProgrami(program, GL_ACTIVE_UNIFORMS);
        IntBuffer size = createIntBuffer(1);
        IntBuffer type = createIntBuffer(1);
        for (int i = 0; i < count; i++) {
            String name = glGetActiveUniform(program, i, size, type);
            int location = glGetUniformLocation(program, name);
            uniforms.add(new Uniform(name, size.get(0), type.get(0), location));
        }
    }

    private int createProgram() {
        return glCreateProgram();
    }

    private void link() {
        glLinkProgram(program);
    }

    private void attachShaders() {
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
    }

    public void putFloat(float value, String uniform) {
        int loc = getUniformLocation(uniform);
        if (loc != -1) glUniform1f(loc, value);
    }

    public void putVector(Vector3f vector, String uniform) {
        int loc = getUniformLocation(uniform);
        if (loc != -1) glUniform3f(loc, vector.x, vector.y, vector.z);
    }

    public void putBool(boolean value, String uniform) {
        putFloat(value?1:0, uniform);
    }

    public void putMatrix(Matrix4f matrix, String uniform) {
        int loc = getUniformLocation(uniform);
        float[] matrixData = new float[16];
        matrix.get(matrixData);
        if (loc != -1) glUniformMatrix4fv(loc, false, matrix.get(matrixData));
    }

    public void putTexture(Texture texture) {
        glActiveTexture(GL_TEXTURE0);
        texture.bind();
    }

    private int getUniformLocation(String name) {
        for (Uniform u : uniforms) {
            if (u.name().equals(name)) return u.location();
        }
        return -1;
    }

    public void start() {
        String err = glGetProgramInfoLog(program);
        if (!err.equals("")) System.out.println(err);
        glUseProgram(program);
        ShaderProgram.setActiveShader(this);
    }

    public void stop() {
        glUseProgram(0);
    }

    public int getID() {
        return program;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Uniform> getUniforms() {
        return uniforms;
    }

    public static ShaderProgram getActiveShader() {
        return activeShader;
    }

    public static void setActiveShader(ShaderProgram activeShader) {
        ShaderProgram.activeShader = activeShader;
    }

    public static ShaderProgram getDefaultShader() {
        return defaultShader;
    }
}
