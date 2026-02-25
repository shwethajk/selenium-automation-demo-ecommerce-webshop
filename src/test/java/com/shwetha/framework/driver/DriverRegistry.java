// src/test/java/com/shwetha/framework/driver/DriverRegistry.java
package com.shwetha.framework.driver;

import org.openqa.selenium.WebDriver;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DriverRegistry {
    private static final Set<WebDriver> LIVE_DRIVERS = ConcurrentHashMap.newKeySet();
    private static final Set<String> TEMP_PROFILES   = ConcurrentHashMap.newKeySet();

    private DriverRegistry() {}

    public static void register(WebDriver d) {
        if (d != null) LIVE_DRIVERS.add(d);
    }
    public static void unregister(WebDriver d) {
        if (d != null) LIVE_DRIVERS.remove(d);
    }

    public static void registerTempProfile(String path) {
        if (path != null && !path.isBlank()) TEMP_PROFILES.add(path);
    }

    /** Close everything that still looks alive (suite end / shutdown hook). */
    public static void killAll() {
        for (WebDriver d : LIVE_DRIVERS.toArray(new WebDriver[0])) {
            try { d.quit(); } catch (Throwable ignore) {}
            LIVE_DRIVERS.remove(d);
        }
        // best-effort profile cleanup
        for (String p : TEMP_PROFILES.toArray(new String[0])) {
            try { com.shwetha.framework.utils.FileUtils.deleteRecursive(p); } catch (Throwable ignore) {}
            TEMP_PROFILES.remove(p);
        }
    }
}