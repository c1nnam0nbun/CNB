package core;

import ecs.Camera;
import ecs.Entity;
import ecs.Light;
import ecs.components.MeshComponent;
import ecs.components.RendererComponent;
import ecs.components.TransformComponent;
import graphics.Material;
import graphics.Mesh;
import opengl.ShaderProgram;
import org.joml.Vector3f;
import util.Maths;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class CNBEngine {

    public static void run(Window window) throws Exception {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        Input.init();
        ShaderProgram.initDefaultShader();
        loop(window);
        window.cleanUp();
    }

    private static void loop(Window window) {
        float[] position = new float[] {
                -0.5f, 0.5f,0f,
                -0.5f,-0.5f,0f,
                0.5f,-0.5f,0f,
                0.5f, 0.5f,0f,
                0f,  -1f,  0f
        };
        float[] uvs = new float[] {
                0f, 0f,
                0f, 1f,
                1f, 1f,
                1f, 0f
        };
        int[] indices = new int[] {0, 1, 2, 2, 0, 3};
        float[] normals = new float[] {
                0f, 0f, 1f,
                0f, 0f, 1f,
                0f, 0f, 1f,
                0f, 0f, 1f,
                0f, 0f, 1f,
        };

        //Vector3f rot;
        //int num = 0;

        Entity objEnt;
        Camera camera = new Camera(window.getWidth(), window.getHeight());
        Camera camera1 = new Camera(window.getWidth(), window.getHeight());
        camera.transform().move(Maths.vectorBack().mul(100));
        camera1.transform().move(Maths.vectorBack().mul(300));
        Light light = new Light(new TransformComponent(Maths.vectorBack().mul(150), Maths.vectorZero(), Maths.vectorOne()), Maths.vectorOne());
        MasterRenderer.light = light;


        Mesh quadMesh = new Mesh(position, indices, normals , uvs);
        Entity quadEnt = new Entity();
        quadEnt.addComponent(new MeshComponent(quadMesh));
        quadEnt.addComponent(new RendererComponent());
        quadEnt.transform().move(Maths.vectorLeft().mul(0.7f));

        List<Entity> batch = new ArrayList<>();

        long start = System.currentTimeMillis();
//        Mesh cube = ResourceManager.getMesh("cube");
        Mesh cube = ResourceManager.getMesh("Sting-Sword-lowpoly");
        System.out.println("GetMesh: " + (System.currentTimeMillis() - start) + " ms");

        for (int i = 0; i < 1; i++) {
            objEnt = new Entity(new TransformComponent(Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne()));
            Material material = new Material();
            objEnt.addComponent(new MeshComponent(cube));
            objEnt.addComponent(new RendererComponent(material));
            batch.add(objEnt);
        }

//        Mesh tree = OBJParser.parse("Lowpoly_tree_sample");
//
//        for (int i = 0; i < 2500; i++) {
//            objEnt = new Entity(new TransformComponent(Maths.random(), Maths.random(), Maths.vectorOne()));
//            Material material = new Material();
//            objEnt.addComponent(new MeshComponent(tree));
//            objEnt.addComponent(new RendererComponent(material));
//            batch.add(objEnt);
//        }


//        switch (num) {
//            case 0 -> {
//                Mesh cube = OBJParser.parse("cube");
//                for (int i = 0; i < 5000; i++) {
//                    objEnt = new Entity(new TransformComponent(Maths.random(), Maths.random(), Maths.vectorOne()));
//                    objEnt.addComponent(new MeshComponent(cube));
//                    objEnt.addComponent(new RendererComponent());
//                    batch.add(objEnt);
//                }
//            }
//            case 1 -> {
//                Mesh sword = OBJParser.parse("Sting-Sword-lowpoly");
//                objEnt = new Entity();
//                objEnt.addComponent(new MeshComponent(sword));
//                objEnt.addComponent(new RendererComponent());
//                objEnt.transform().rotate(Maths.vectorRight().mul(50));
//                rot = new Vector3f(0, 0, 1);
//            }
//            case 2 -> {
//                Mesh tree = OBJParser.parse("Lowpoly_tree_sample");
//
//                for (int i = 0; i < 10000; i++) {
//                    objEnt = new Entity(new TransformComponent(Maths.random(), Maths.random(), Maths.vectorOne()));
//                    objEnt.addComponent(new MeshComponent(tree));
//                    objEnt.addComponent(new RendererComponent());
//                    batch.add(objEnt);
//                }
//            }
//            case 3 -> {
//                Mesh table = OBJParser.parse("Главный стол+ПК");
//                objEnt = new Entity(new TransformComponent(Maths.vectorZero(), Maths.vectorZero(), Maths.vectorOne().mul(.05f)));
//                objEnt.addComponent(new MeshComponent(table));
//                objEnt.addComponent(new RendererComponent());
//                rot = new Vector3f(0, 1, 0);
//            }
//            default -> throw new IllegalStateException("Unexpected value: " + num);
//        }

        MasterRenderer.registerEntity(quadEnt);
        MasterRenderer.registerEntityList(batch);
        MasterRenderer.registerCamera(camera);
        MasterRenderer.registerCamera(camera1);
        MasterRenderer.setActiveCamera(camera);
        MasterRenderer.registerLight(light);

        Time.init();

        while (!glfwWindowShouldClose(window.getHandle())) {
            Time.update();   
            MasterRenderer.update(window);

            Time.frames++;

            if (System.currentTimeMillis() - Time.timer > 1000) {
                Time.timer += 1000;
                window.setTitle(Time.frames + " fps");
                Time.frames = 0;
            }

            for (Entity entity : batch) {
                Vector3f newRot = new Vector3f();
                Maths.vectorLeft().mul(Time.deltaTime * 100, newRot);
                entity.transform().rotate(newRot);
            }
        }
    }
}