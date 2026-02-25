// package com.shwetha.framework.utils;

// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import java.io.InputStream;
// import java.util.*;
// import java.util.stream.Collectors;

// /**
//  * Loads test data arrays from JSON resources into List<Map<String,String>>
//  * and converts them to Object[][] for TestNG DataProviders.
//  *
//  * Expected JSON format: an array of JSON objects. Example:
//  * [
//  *   {"TC_ID":"L001","Execute":"Y","Username":"...", "Password":"...", "ExpectedResult":"SUCCESS"},
//  *   ...
//  * ]
//  */
// public final class JsonData {
//     private static final ObjectMapper MAPPER = new ObjectMapper();

//     private JsonData(){}

//     /** Loads a JSON array (of objects) from classpath, returns List of String maps (values normalized to String). */
//     public static List<Map<String,String>> load(String resourcePath) {
//         // Allow resources under src/test/resources without leading slash
//         String normalized = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;

//         try (InputStream in = Thread.currentThread()
//                                     .getContextClassLoader()
//                                     .getResourceAsStream(normalized)) {
//             if (in == null) {
//                 throw new RuntimeException("Data file not found on classpath: " + normalized);
//             }
//             List<Map<String, Object>> raw = MAPPER.readValue(in, new TypeReference<>() {});
//             return raw.stream()
//                       .map(JsonData::toStringMap)
//                       .collect(Collectors.toCollection(ArrayList::new));
//         } catch (Exception e) {
//             throw new RuntimeException("Failed to read JSON: " + normalized, e);
//         }
//     }

//     /** Default DataProvider array: filters Execute=Y (case-insensitive), one Map per Object[] row. */
//     public static Object[][] asDataProviderArray(List<Map<String,String>> rows) {
//         List<Object[]> filtered = rows.stream()
//                 .filter(m -> "Y".equalsIgnoreCase(m.getOrDefault("Execute", "Y").trim()))
//                 .map(m -> new Object[]{ m })
//                 .collect(Collectors.toList());
//         return filtered.toArray(new Object[0][]);
//     }

//     /** Helper: deep-cast Map<String,Object> to Map<String,String> while preserving insertion order. */
//     private static Map<String,String> toStringMap(Map<String,Object> in) {
//         Map<String,String> out = new LinkedHashMap<>();
//         in.forEach((k, v) -> out.put(k, v == null ? "" : String.valueOf(v)));
//         return out;
//     }
// }



package com.shwetha.framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Loads test data arrays from JSON resources into List<Map<String,String>>.
 * JSON format per dataset: an array of objects (rows).
 */
public final class JsonData {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonData(){}

    /** Load JSON array of objects from either classpath or filesystem. */
    public static List<Map<String,String>> load(String baseDir, String fileNameNoExt) {
        String fileName = fileNameNoExt.endsWith(".json") ? fileNameNoExt : fileNameNoExt + ".json";
        String raw = normalize(baseDir) + fileName;

        // 1) Try classpath
        List<Map<String,String>> fromCp = tryClasspath(raw);
        if (fromCp != null) return fromCp;

        // 2) Try filesystem
        return fromFs(raw);
    }

    private static List<Map<String,String>> tryClasspath(String resourcePath) {
        String normalized = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(normalized)) {
            if (in == null){
                throw new RuntimeException("JSON NOT FOUND: " + normalized + " (Check jsonData path)");
                // return null;
            }
            List<Map<String, Object>> raw = new ObjectMapper().readValue(in, new TypeReference<>() {});
            return raw.stream().map(JsonData::toStringMap).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON from classpath: " + normalized, e);
        }
    }

    private static List<Map<String,String>> fromFs(String path) {
        try (InputStream in = Files.newInputStream(Paths.get(path))) {
            List<Map<String, Object>> raw = MAPPER.readValue(in, new TypeReference<>() {});
            return raw.stream().map(JsonData::toStringMap).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON from filesystem: " + path, e);
        }
    }

    private static String normalize(String dir) {
        if (dir == null) return "";
        String d = dir.trim();
        // If user passed "src/test/resources/testdata/JSON/", convert to classpath-relative "testdata/JSON/"
        d = d.replace('\\', '/');
        if (d.startsWith("src/test/resources/")) d = d.substring("src/test/resources/".length());
        if (!d.isEmpty() && !d.endsWith("/")) d += "/";
        return d;
    }

    private static Map<String,String> toStringMap(Map<String,Object> in) {
        Map<String,String> out = new LinkedHashMap<>();
        in.forEach((k, v) -> out.put(k, v == null ? "" : String.valueOf(v)));
        return out;
    }
}