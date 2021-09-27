package graphics;

import core.ResourceManager;
import opengl.ShaderProgram;
import opengl.Texture;
import org.joml.Vector3f;
import util.Maths;

public class Material {

    private ShaderProgram shader;

    private Vector3f diffuse;
    private Vector3f ambient;
    private Vector3f specular;
    private Vector3f shininess;
    private Texture diffuseTexture;

    public Material() {
        this.shader = ShaderProgram.getDefaultShader();
        diffuseTexture = ResourceManager.getTexture("res/on_off2.jpg");
    }

    public Material(ShaderProgram shader) {
        this.shader = shader;
    }

    public void apply() {
        diffuse = diffuse == null ? Maths.randomUnsignedVector().normalize() : diffuse;
        if (diffuseTexture != null) shader.putTexture(diffuseTexture);
        shader.putVector(diffuse, "material.diffuseColor");
        shader.putVector(Maths.vectorOne().mul(.02f), "material.specular");
        shader.putVector(Maths.vectorZero(), "material.ambient");
        shader.putFloat(1.0f, "material.shininess");
    }

    public ShaderProgram getShader() {
        return shader;
    }
}
