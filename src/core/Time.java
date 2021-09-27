package core;

public class Time {

    private static long lastTime;
    static long timer;
    private static final double ms = 1000000000.0;
    static float deltaTime;
    static int frames;

    public static void init() {
        lastTime = System.nanoTime();
        timer = System.currentTimeMillis();
    }

    public static void update() {
        long now = System.nanoTime();
        deltaTime = (float) ((now - lastTime) / ms);
        lastTime = now;
    }
}
