// // // package com.shwetha.framework.listeners;

// // // import com.shwetha.framework.utils.ConfigReader;
// // // import org.testng.IAlterSuiteListener;
// // // import org.testng.xml.XmlSuite;
// // // import java.util.List;

// // // public class ParallelSuiteConfigurer implements IAlterSuiteListener {

// // //     @Override
// // //     public void alter(List<XmlSuite> suites) {
// // //         boolean enabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
// // //         if (!enabled) return;

// // //         String mode = ConfigReader.get("suite.parallel", "methods").toLowerCase().trim();
// // //         XmlSuite.ParallelMode pm = switch (mode) {
// // //             case "classes"   -> XmlSuite.ParallelMode.CLASSES;
// // //             case "tests"     -> XmlSuite.ParallelMode.TESTS;
// // //             case "instances" -> XmlSuite.ParallelMode.INSTANCES;
// // //             default          -> XmlSuite.ParallelMode.METHODS; // sensible default
// // //         };

// // //         int threads   = parseIntOr(ConfigReader.get("suite.thread.count"),  Runtime.getRuntime().availableProcessors());
// // //         int dpThreads = parseIntOr(ConfigReader.get("suite.dp.thread.count"), threads);

// // //         for (XmlSuite s : suites) {
// // //             s.setParallel(pm);
// // //             s.setThreadCount(threads);
// // //             s.setDataProviderThreadCount(dpThreads);
// // //             // keep ordering stable unless you want maximal sharding
// // //             s.setPreserveOrder(true);
// // //         }
// // //     }

// // //     private static int parseIntOr(String s, int def) {
// // //         try { return Integer.parseInt(s); } catch (Exception e) { return def; }
// // //     }
// // // }


// // // src\test\java\com\shwetha\framework\listeners\ParallelSuiteConfigurer.java
// // package com.shwetha.framework.listeners;

// // import com.shwetha.framework.utils.ConfigReader;
// // import org.testng.IAlterSuiteListener;
// // import org.testng.xml.XmlSuite;

// // import java.util.List;

// // public class ParallelSuiteConfigurer implements IAlterSuiteListener {

// //     @Override
// //     public void alter(List<XmlSuite> suites) {
// //         boolean enabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
// //         if (!enabled) return;

// //         String mode = ConfigReader.get("suite.parallel", "methods").toLowerCase().trim();
// //         XmlSuite.ParallelMode pm = switch (mode) {
// //             case "classes"   -> XmlSuite.ParallelMode.CLASSES;
// //             case "tests"     -> XmlSuite.ParallelMode.TESTS;
// //             case "instances" -> XmlSuite.ParallelMode.INSTANCES;
// //             default          -> XmlSuite.ParallelMode.METHODS;
// //         };

// //         int threads   = parseIntOr(ConfigReader.get("suite.thread.count"),  Runtime.getRuntime().availableProcessors());
// //         int dpThreads = parseIntOr(ConfigReader.get("suite.dp.thread.count"), threads);

// //         for (XmlSuite s : suites) {
// //             s.setParallel(pm);
// //             s.setThreadCount(threads);
// //             s.setDataProviderThreadCount(dpThreads);
// //             s.setPreserveOrder(true);
// //         }
// //     }

// //     private static int parseIntOr(String s, int def) {
// //         try { return Integer.parseInt(s); } catch (Exception e) { return def; }
// //     }
// // }



// // src/test/java/com/shwetha/framework/listeners/ParallelSuiteConfigurer.java
// package com.shwetha.framework.listeners;

// import com.shwetha.framework.utils.ConfigReader;
// import org.testng.IAlterSuiteListener;
// import org.testng.xml.XmlSuite;

// import java.util.List;

// public class ParallelSuiteConfigurer implements IAlterSuiteListener {
//     @Override
//     public void alter(List<XmlSuite> suites) {
//         boolean enabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
//         if (!enabled) return;

//         int dpThreads = parseIntOr(ConfigReader.get("suite.dp.thread.count"),
//                 Runtime.getRuntime().availableProcessors());

//         for (XmlSuite s : suites) {
//             s.setDataProviderThreadCount(dpThreads);
//             s.setPreserveOrder(true);
//         }
//     }

//     private static int parseIntOr(String s, int def) {
//         try { return Integer.parseInt(s); } catch (Exception e) { return def; }
//     }
// }




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