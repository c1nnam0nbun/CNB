package tests;

import opengl.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OBJParserUpdated {

    public static void parse(String name) {

        List<Vector3f> positions = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> uvs = new ArrayList<>();

        Map<String, List<String>> submeshData = new HashMap<>();
        Map<String, HashMap<List<Vertex>, int[]>> submeshes = new HashMap<>();
        List<String> data = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();

        long start = System.currentTimeMillis();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("res/" + name + ".obj"));
            String line, mat = "";
            while((line = reader.readLine()) != null)  {
                if (line.startsWith("usemtl ")) {
                    mat = line.split(" ")[1];
                    data.clear();
                    submeshData.put(mat, data);
                    submeshes.put(mat, null);
                }
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f position = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    positions.add(position);
                }
                if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f uv = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    uvs.add(uv);
                }
                if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                }
                if (line.startsWith("f ") && !mat.equals("") ) {
                    submeshData.get(mat).add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : submeshData.keySet()) {
            Thread thread = new Thread(() -> {
                List<Vertex> vertices = new ArrayList<>();
                List<Integer> indices = new ArrayList<>();
                for (String line : submeshData.get(key)) {
                    String[] currentLine = line.split(" ");
                    for (int i = 1; i < currentLine.length; i++) {
                        String[] split = currentLine[i].split("/");
                        int pointer = Integer.parseInt(split[0]) - 1;
                        Vector3f pos = positions.get(pointer);
                        Vector2f tex = uvs.get(Integer.parseInt(split[1]) - 1);
                        Vector3f normal = normals.get(Integer.parseInt(split[2]) - 1);
                        Vertex vertex = new Vertex(pos, tex, normal);
                        if (!vertices.contains(vertex)) {
                            vertices.add(vertex);
                        }
                        indices.add(vertices.indexOf(vertex));
                    }
                }
                int[] indicesArray = new int[indices.size()];

                for (int i = 0; i < indices.size(); i++) {
                    indicesArray[i] = indices.get(i);
                }
                HashMap<List<Vertex>, int[]> map = new HashMap<>();
                map.put(vertices, indicesArray);
                submeshes.put(key, map);
            });
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("UPD: " + (System.currentTimeMillis() - start) + " ms");
    }
}
