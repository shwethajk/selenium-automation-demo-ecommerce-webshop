
package com.shwetha.framework.utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    // private static final Properties props = new Properties();
    public static Properties props = new Properties();

    public static void load() {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String get(String key, String defVal) {
        String raw = props.getProperty(key);
        if (raw == null) return defVal;
        String cleaned = stripInlineComment(raw).trim();
        return cleaned.isEmpty() ? defVal : cleaned;
    }

    // Helper method
    private static String stripInlineComment(String s) {
        if (s == null) return null;
        // Remove any in-line comment that starts with whitespace followed by '#' or ';'
        return s.replaceAll("\\s+[#;].*$", "");
    }
}