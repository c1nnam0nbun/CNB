package graphics.parsers;

import graphics.Mesh;
import opengl.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class OBJParser {

    public static Mesh parse(String fileName){
        List<Vector3f> positions = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> uvs = new ArrayList<>();

        List<Vertex> vertices = new ArrayList<>();

        Thread fileProcessor = new Thread(() -> {
            List<String> v = new ArrayList<>();
            List<String> vt = new ArrayList<>();
            List<String> vn = new ArrayList<>();
            List<String> f = new ArrayList<>();

            List<Future<?>> futures = new ArrayList<>();

            try {
                BufferedReader reader = new BufferedReader(new FileReader("res/" + fileName + ".obj"));
                String line;
                while((line = reader.readLine()) != null)  {
                    if (line.startsWith("v ")) v.add(line);
                    else if (line.startsWith("vt ")) vt.add(line);
                    else if (line.startsWith("vn ")) vn.add(line);
                    else if (line.startsWith("f ")) f.add(line);
                }
                System.out.println(f.size());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
            executor.setMaximumPoolSize(30);
            futures.add(executor.submit(() -> {
                for (String line : v) {
                    String[] currentLine = line.split(" ");
                    Vector3f position = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    positions.add(position);
                }
                //System.out.println("Positions: \n" + positions);
            }));
            futures.add(executor.submit(() -> {
                for (String line : vt) {
                    String[] currentLine = line.split(" ");
                    Vector2f uv = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    uvs.add(uv);
                }
                //System.out.println("UVs: \n" + uvs);
            }));
            futures.add(executor.submit(() -> {
                for (String line : vn) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                }
                //System.out.println("Normals: \n" + normals);
            }));

            for (Future<?> ft : futures) {
                try {
                    ft.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
//
//            int size = 0;
//            for (int i = 5; i < 30; i++) {
//                if (f.size() % i == 0) {
//                    size = i;
//                    break;
//                }
//            }
//            System.out.println(size);
//            executor.setCorePoolSize(size);
//
//            for (int i = 0; i < f.size() * 3; i++) {
//                vertices.add(null);
//            }
//
//            int idx = 0;
//            for (int i = 0; i < executor.getPoolSize(); i++) {
//                int finalIdx = idx;
//                int finalI = i + 1;
//                futures.add(executor.submit(() -> {
//                    for (int j = finalIdx; j < f.size() / executor.getPoolSize() * finalI; j++) {
//                        String[] currentLine = f.get(j).split(" ");
//                        for (int k = 1; k < currentLine.length; k++) {
//                            String[] data = currentLine[k].split("/");
//                            int pointer = Integer.parseInt(data[0]) - 1;
//                            Vector3f pos = positions.get(pointer);
//                            Vector2f tex = uvs.get(Integer.parseInt(data[1]) - 1);
//                            Vector3f normal = normals.get(Integer.parseInt(data[2]) - 1);
//                            Vertex vertex = new Vertex(pos, tex, normal);
//                            vertices.set(j, vertex);
//                        }
//                    }
//                }));
//                idx = f.size() / executor.getPoolSize();
//            }
//
//            for (Future<?> ft : futures) {
//                try {
//                    ft.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
////            System.out.println(vertices.indexOf(null));
            executor.shutdown();

            for (String line : f) {
                String[] currentLine = line.split(" ");
                for (int i = 1; i < currentLine.length; i++) {
                    String[] data = currentLine[i].split("/");
                    int pointer = Integer.parseInt(data[0]) - 1;
                    Vector3f pos = positions.get(pointer);
                    Vector2f tex = uvs.get(Integer.parseInt(data[1]) - 1);
                    Vector3f normal = normals.get(Integer.parseInt(data[2]) - 1);
                    Vertex vertex = new Vertex(pos, tex, normal);
                    if (!vertices.contains(vertex)) {
                        vertices.add(vertex);
                    }
                    indices.add(vertices.indexOf(vertex));
                }
            }
        });

        fileProcessor.start();

        try {
            fileProcessor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int[] indicesArray = new int[indices.size()];

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        Mesh mesh = new Mesh(vertices, indicesArray);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName + ".cnm");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mesh);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mesh;
    }
}
