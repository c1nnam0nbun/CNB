package core;

import graphics.Mesh;
import graphics.parsers.OBJParser;
import opengl.Texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    private static final Map<String, Texture> textures = new HashMap<>();
    private static final Map<String, Mesh> meshes = new HashMap<>();

    public static Texture getTexture(String path) {
        Texture texture;
        if (textures.containsKey(path)) texture = textures.get(path);
        else {
            texture = Texture.loadTexture(path);
            textures.put(path, texture);
        }
        return texture;
    }

    public static Mesh getMesh(String name) {
        Mesh mesh = null;
        if (meshes.containsKey(name)) mesh = meshes.get(name);
        else if (new File(name + ".cnm").exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(name + ".cnm");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Mesh des = (Mesh) objectInputStream.readObject();
                mesh = new Mesh(des.getPositions(), des.getIndices(), des.getNormals(), des.getUvs());
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            mesh = OBJParser.parse(name);
            meshes.put(name, mesh);
        }
        return mesh;
    }
}
