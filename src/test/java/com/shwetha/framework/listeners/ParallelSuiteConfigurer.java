/*

// src/test/java/com/shwetha/framework/listeners/ParallelSuiteConfigurer.java
package com.shwetha.framework.listeners;

import com.shwetha.framework.utils.ConfigReader;
import org.testng.IAlterSuiteListener;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

import java.util.List;

public class ParallelSuiteConfigurer implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {
        boolean enabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false").trim());
        for (XmlSuite s : suites) {
            if (!enabled) {
                // Force everything to be sequential irrespective of XML
                s.setParallel(XmlSuite.ParallelMode.NONE);
                s.setThreadCount(1);
                s.setDataProviderThreadCount(1);
                s.setPreserveOrder(true);
                // continue;
            }
            else {
                // When enabled, read counts from config
                String suiteMode = ConfigReader.get("suite.parallel", "methods").trim().toLowerCase();
                int tc = parseIntOr(ConfigReader.get("suite.thread.count"), Runtime.getRuntime().availableProcessors());
                int dp = parseIntOr(ConfigReader.get("suite.dp.thread.count"), tc);

                // Apply parallel mode
                switch (suiteMode) {
                    case "classes"   -> s.setParallel(XmlSuite.ParallelMode.CLASSES);
                    case "tests"     -> s.setParallel(XmlSuite.ParallelMode.TESTS);
                    case "instances" -> s.setParallel(XmlSuite.ParallelMode.INSTANCES);
                    case "methods"   -> s.setParallel(XmlSuite.ParallelMode.METHODS);
                    default          -> s.setParallel(XmlSuite.ParallelMode.NONE);
                }
                s.setThreadCount(tc);
                s.setDataProviderThreadCount(dp);
                s.setPreserveOrder(true);
            }
            // 👉 Publish effective values for other components to consume
            System.setProperty("suite.effective.parallel.mode", s.getParallel().name());               // NONE | METHODS | ...
            System.setProperty("suite.effective.thread.count", String.valueOf(s.getThreadCount()));     // e.g., 1
            System.setProperty("suite.effective.dp.thread.count", String.valueOf(s.getDataProviderThreadCount())); // e.g., 1
        }
    }
    private static int parseIntOr(String s, int def) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }
}
*/


// // src/test/java/com/shwetha/framework/listeners/ParallelSuiteConfigurer.java
// package com.shwetha.framework.listeners;

// import com.shwetha.framework.utils.ConfigReader;
// import org.testng.IAlterSuiteListener;
// import org.testng.xml.XmlSuite;
// import java.util.List;

// public class ParallelSuiteConfigurer implements IAlterSuiteListener {

//     @Override
//     public void alter(List<XmlSuite> suites) {
        // ConfigReader.load();
//         boolean enabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false").trim());
//         for (XmlSuite s : suites) {
//             if (!enabled) {
//                 // Force everything to be sequential irrespective of XML
//                 s.setParallel(XmlSuite.ParallelMode.NONE);
//                 s.setThreadCount(1);
//                 s.setDataProviderThreadCount(1);
//                 // s.setPreserveOrder(true);
//                 s.setPreserveOrder(false);
//             }
//             else {
//                 // When enabled, read counts from config
//                 String suiteMode = ConfigReader.get("suite.parallel", "methods").trim().toLowerCase();
//                 int tc = parseIntOr(ConfigReader.get("suite.thread.count"), Runtime.getRuntime().availableProcessors());
//                 int dp = parseIntOr(ConfigReader.get("suite.dp.thread.count"), tc);

//                 // Apply parallel mode
//                 switch (suiteMode) {
//                     case "classes"   -> s.setParallel(XmlSuite.ParallelMode.CLASSES);
//                     case "tests"     -> s.setParallel(XmlSuite.ParallelMode.TESTS);
//                     case "instances" -> s.setParallel(XmlSuite.ParallelMode.INSTANCES);
//                     case "methods"   -> s.setParallel(XmlSuite.ParallelMode.METHODS);
//                     default          -> s.setParallel(XmlSuite.ParallelMode.NONE);
//                 }
//                 s.setThreadCount(tc);
//                 s.setDataProviderThreadCount(dp);
//                 // s.setPreserveOrder(true);
//                 s.setPreserveOrder(false);   // <-- was true
//             }
//             // 👉 Publish effective values for other components to consume
//             System.setProperty("suite.effective.parallel.mode", s.getParallel().name());               // NONE | METHODS | ...
//             System.setProperty("suite.effective.thread.count", String.valueOf(s.getThreadCount()));     // e.g., 1
//             System.setProperty("suite.effective.dp.thread.count", String.valueOf(s.getDataProviderThreadCount())); // e.g., 1
//         }
//     }
//     private static int parseIntOr(String s, int def) {
//         try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
//     }
// }



// src/test/java/com/shwetha/framework/listeners/ParallelSuiteConfigurer.java
package com.shwetha.framework.listeners;

import com.shwetha.framework.utils.ConfigReader;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import java.util.List;

public class ParallelSuiteConfigurer implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {
        ConfigReader.load();

        // If you set parallel.override=true, we will force settings from properties.
        // If false (default), we "assist": respect XML and only fill gaps.
        boolean configOverride = Boolean.parseBoolean(ConfigReader.get("parallel.override", "false").trim());
        boolean parallelEnabled  = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false").trim());

        String desiredParallelLevel  = ConfigReader.get("suite.parallel", "methods").trim().toLowerCase();
        int    configThreadCount    = parseIntOr(ConfigReader.get("suite.thread.count"), Runtime.getRuntime().availableProcessors());
        int    configDPThreadCount    = parseIntOr(ConfigReader.get("suite.dp.thread.count"), configThreadCount);

        for (XmlSuite s : suites) {
            // Derive desired suite parallel mode
            XmlSuite.ParallelMode configParallelLevel =
                    switch (desiredParallelLevel) {
                        case "classes"   -> XmlSuite.ParallelMode.CLASSES;
                        case "tests"     -> XmlSuite.ParallelMode.TESTS;
                        case "instances" -> XmlSuite.ParallelMode.INSTANCES;
                        case "methods"   -> XmlSuite.ParallelMode.METHODS;
                        default          -> XmlSuite.ParallelMode.NONE;
                    };

            if (!parallelEnabled) {
                // Force sequential when explicitly disabled
                s.setParallel(XmlSuite.ParallelMode.NONE);
                s.setThreadCount(1);
                s.setDataProviderThreadCount(1);
                // s.setPreserveOrder(false);
                s.setPreserveOrder(true);

                for (XmlTest t : s.getTests()) {
                    t.setParallel(XmlSuite.ParallelMode.NONE);
                    t.setThreadCount(1);
                    // t.setPreserveOrder(false);
                    t.setPreserveOrder(true);
                }
            } else {
                // Enabled - Apply in "assist" or "override" mode
                if (configOverride) {
                    // Force from config properties (override XML)
                    s.setParallel(configParallelLevel);
                    s.setThreadCount(configThreadCount);
                    s.setDataProviderThreadCount(configDPThreadCount);
                } else {
                    // Assist mode: only set if XML isn't already parallel
                    if (s.getParallel() == null || s.getParallel() == XmlSuite.ParallelMode.NONE) {
                        s.setParallel(configParallelLevel);
                    }
                    if (s.getThreadCount() <= 1 && configThreadCount > 1) {
                        s.setThreadCount(configThreadCount);
                    }
                    if (s.getDataProviderThreadCount() <= 1 && configDPThreadCount > 1) {
                        s.setDataProviderThreadCount(configDPThreadCount);
                    }
                }

                s.setPreserveOrder(false);

                // Always propagate down to tests if tests lack their own parallel
                for (XmlTest t : s.getTests()) {
                    if (configOverride) {
                        t.setParallel(s.getParallel());
                        t.setThreadCount(s.getThreadCount());
                    } else {
                        if (t.getParallel() == null || t.getParallel() == XmlSuite.ParallelMode.NONE) {
                            t.setParallel(s.getParallel());
                        }
                        if (t.getThreadCount() <= 1 && s.getThreadCount() > 1) {
                            t.setThreadCount(s.getThreadCount());
                        }
                    }
                    t.setPreserveOrder(false);
                }
            }

            // Publish effective values
            System.setProperty("suite.effective.parallel.mode", s.getParallel().name());
            System.setProperty("suite.effective.thread.count", String.valueOf(s.getThreadCount()));
            System.setProperty("suite.effective.dp.thread.count", String.valueOf(s.getDataProviderThreadCount()));
        }
    }

    private static int parseIntOr(String s, int def) {
        try { return Integer.parseInt(String.valueOf(s).trim()); } catch (Exception e) { return def; }
    }
}


