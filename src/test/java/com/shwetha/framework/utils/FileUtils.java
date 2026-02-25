// src/test/java/com/shwetha/framework/utils/FileUtils.java
package com.shwetha.framework.utils;

import java.io.IOException;
import java.nio.file.*;

public final class FileUtils {
    private FileUtils() {}
    public static void deleteRecursive(String path) throws IOException {
        Path root = Paths.get(path);
        if (!Files.exists(root)) return;
        Files.walk(root)
             .sorted((a,b) -> b.getNameCount() - a.getNameCount()) // children first
             .forEach(p -> { try { Files.deleteIfExists(p); } catch (IOException ignored) {} });
    }
}