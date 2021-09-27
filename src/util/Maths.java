package util;

import org.joml.Matrix4f;
import org.joml.Random;
import org.joml.Vector3f;

public class Maths {
    private static Random random = new Random();

    public static Vector3f randomUnsignedVector() {
        Vector3f vector = new Vector3f();
        vector.x = getRandomFloat(0, 100);
        vector.y = getRandomFloat(0, 100);
        vector.z = getRandomFloat(0, 100);
        return vector;
    }

    public static Vector3f randomVector() {
        Vector3f vector = new Vector3f();
        vector.x = random.nextFloat() * 100 - 50;
        vector.y = random.nextFloat() * 100 - 50;
        vector.z = random.nextFloat() * 100 - 50;
        return vector;
    }

    public static float getRandomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static Vector3f vectorUp() {
        return new Vector3f(0, 1, 0);
    }

    public static Vector3f vectorDown() {
        return new Vector3f(0, -1, 0);
    }

    public static Vector3f vectorLeft() {
        return new Vector3f(-1, 0, 0);
    }

    public static Vector3f vectorRight() {
        return new Vector3f(1, 0, 0);
    }

    public static Vector3f vectorForward() {
        return new Vector3f(0, 0, -1);
    }

    public static Vector3f vectorBack() {
        return new Vector3f(0, 0, 1);
    }

    public static Vector3f vectorZero() {
        return new Vector3f(0, 0, 0);
    }

    public static Vector3f vectorOne() {
        return new Vector3f(1, 1, 1);
    }
}
