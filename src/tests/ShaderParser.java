package tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShaderParser {

    public static void parse(String path) {
        Map<String, List<String>> shaders = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line, type = null;
            while ((line = reader.readLine()) != null) {
                if (line.matches("Shader \"(.*)\"\s*\\{")) {
                    type = line.split(" ")[1];
                    type = type.substring(1, type.length() - 1);
                }
                if (type != null && !line.matches("Shader \"(.*)\"\s*\\{")) {
                    shaders.computeIfAbsent(type, k -> new ArrayList<>());
                    shaders.get(type).add(line.trim());
                }
            }
        } catch (IOException e) { e.printStackTrace(); }

        for (String type : shaders.keySet()) processShader(type, shaders.get(type));
    }

    private static void processShader(String type, List<String> source) {
        StringBuilder shader = new StringBuilder();
        if (!isValidType(type)) return;

        List<String> attributes, output, structs, uniforms;

        String start = source.stream().filter(k -> k.startsWith("attributes")).collect(Collectors.joining());
        String end = "};";

        attributes = source.subList(source.indexOf(start) + 1, source.indexOf(end));
        source.remove(start);
        source.remove(end);

        start = source.stream().filter(k -> k.startsWith("output")).collect(Collectors.joining());
        output = source.subList(source.indexOf(start) + 1, source.indexOf(end));
        source.remove(start);
        source.remove(end);

        start = source.stream().filter(k -> k.startsWith("structs")).collect(Collectors.joining());
        structs = source.subList(source.indexOf(start) + 1, source.indexOf(end));
        source.remove(start);
        source.remove(end);

        start = source.stream().filter(k -> k.startsWith("uniforms")).collect(Collectors.joining());
        uniforms = source.subList(source.indexOf(start) + 1, source.indexOf(end));
        source.remove(start);
        source.remove(end);

        AtomicInteger i = new AtomicInteger();
        attributes.forEach(k -> {
            shader.append("layout(location=").append(i).append(") in ").append(k).append("\n");
            i.getAndIncrement();
        });
    }

    private static boolean isValidType(String type) {
        String[] validTypes = {"Vertex", "Fragment", "Geometry"};
        return Arrays.asList(validTypes).contains(type);
    }
}
