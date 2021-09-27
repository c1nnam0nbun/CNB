package util;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.BufferUtils.createFloatBuffer;

public class Util {
    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(Class<T> c) {
        return c.isPrimitive() ? (Class<T>) PrimitivesToWrappers().get(c) : c;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> unwrap(Class<T> c) {
        return c.isPrimitive() ? c : (Class<T>) WrappersToPrimitives().get(c);
    }

    private static HashMap<Class<?>, Class<?>> PrimitivesToWrappers() {
        HashMap<Class<?>, Class<?>> map = new HashMap<>();
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(short.class, Short.class);
        map.put(void.class, Void.class);
        return map;
    }
    private static HashMap<Class<?>, Class<?>> WrappersToPrimitives() {
        HashMap<Class<?>, Class<?>> map = new HashMap<>();
        map.put(Boolean.class, boolean.class);
        map.put(Byte.class, byte.class);
        map.put(Character.class, char.class);
        map.put(Double.class, double.class);
        map.put(Float.class, float.class);
        map.put(Integer.class, int.class);
        map.put(Long.class, long.class);
        map.put(Short.class, short.class);
        map.put(Void.class, void.class);
        return map;
    }

    public static float[] vectorToArray(Vector3f vector) {
        FloatBuffer buffer = createFloatBuffer(3);
        vector.get(buffer);
        return transform(buffer);
    }

    public static float[] vectorToArray(Vector2f vector) {
        FloatBuffer buffer = createFloatBuffer(2);
        vector.get(buffer);
        return transform(buffer);
    }

    public static ArrayList<Float> vectorToList(Vector3f vector) {
        ArrayList<Float> result = new ArrayList<>();
        result.add(vector.x);
        result.add(vector.y);
        result.add(vector.z);
        return result;
    }

    public static ArrayList<Float> vectorToList(Vector2f vector) {
        ArrayList<Float> result = new ArrayList<>();
        result.add(vector.x);
        result.add(vector.y);
        return result;
    }

    private static float[] transform(FloatBuffer buffer) {
        float[] array = new float[buffer.limit()];
        buffer.get(array);

        float[] result = new float[buffer.limit()];
        System.arraycopy(array, 0, result, 0, array.length);

        return result;
    }



}
