// // // // // src\test\java\com\shwetha\framework\base\BaseTests.java

// // // // package com.shwetha.framework.base;

// // // // import com.shwetha.framework.driver.DriverFactory;
// // // // import com.shwetha.framework.utils.ConfigReader;

// // // // import org.apache.logging.log4j.LogManager;
// // // // import org.apache.logging.log4j.Logger;
// // // // import org.openqa.selenium.WebDriver;
// // // // import org.testng.annotations.AfterSuite;
// // // // import org.testng.annotations.BeforeSuite;

// // // // public class BaseTests {

// // // //     public final Logger log = LogManager.getLogger(getClass()); // Use this log in other test classes, as they extend BaseTest class

// // // //     protected static WebDriver driver;

// // // //     @BeforeSuite(alwaysRun = true)
// // // //     public void setUpSuite() {
// // // //         ConfigReader.load();
// // // //         driver = DriverFactory.createDriver();
// // // //     }

// // // //     @AfterSuite(alwaysRun = true)
// // // //     public void tearDownSuite() {
// // // //         if (driver != null) {
// // // //             driver.quit();
// // // //         }
// // // //     }
// // // // }

// // // // src\test\java\com\shwetha\framework\base\BaseTests.java
// // // package com.shwetha.framework.base;

// // // import com.shwetha.framework.driver.DriverFactory;
// // // import com.shwetha.framework.utils.ConfigReader;

// // // import org.apache.logging.log4j.LogManager;
// // // import org.apache.logging.log4j.Logger;
// // // import org.openqa.selenium.WebDriver;
// // // import org.testng.ITestResult;
// // // import org.testng.annotations.AfterMethod;
// // // import org.testng.annotations.BeforeMethod;
// // // import org.testng.annotations.BeforeSuite;

// // // public class BaseTests {

// // //     public final Logger log = LogManager.getLogger(getClass()); // Use this log in other test classes, as they extend BaseTest class

// // //     // --- CHANGED: ThreadLocal instead of one static instance ---
// // //     private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

// // //     protected static WebDriver getDriver() {
// // //         return TL_DRIVER.get();
// // //     }

// // //     @BeforeSuite(alwaysRun = true)
// // //     public void setUpSuite() {
// // //         ConfigReader.load();
// // //     }

// // //     // --- NEW: create a driver per test method/thread ---
// // //     @BeforeMethod(alwaysRun = true)
// // //     public void createDriver() {
// // //         if (getDriver() == null) {
// // //             TL_DRIVER.set(DriverFactory.createDriver());
// // //         }
// // //     }

// // //     // --- NEW: quit driver per test method/thread ---
// // //     @AfterMethod(alwaysRun = true)
// // //     public void quitDriver(ITestResult result) {
// // //         WebDriver d = getDriver();
// // //         try {
// // //             if (d != null) d.quit();
// // //         } finally {
// // //             TL_DRIVER.remove();
// // //         }
// // //     }
// // // }

// // package com.shwetha.framework.base;

// // import com.shwetha.framework.driver.DriverFactory;
// // import com.shwetha.framework.utils.ConfigReader;

// // import org.apache.logging.log4j.LogManager;
// // import org.apache.logging.log4j.Logger;
// // import org.openqa.selenium.WebDriver;
// // import org.testng.ITestResult;
// // import org.testng.annotations.AfterMethod;
// // import org.testng.annotations.BeforeMethod;
// // import org.testng.annotations.BeforeSuite;

// // public class BaseTests {

// //     public final Logger log = LogManager.getLogger(getClass()); // Use this log in other test classes, as they extend BaseTest class

// //     private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

// //     // protected static WebDriver getDriver() {
// //     public static WebDriver getDriver() {
// //         return TL_DRIVER.get();
// //     }

// //     @BeforeSuite(alwaysRun = true)
// //     public void setUpSuite() {
// //         ConfigReader.load();
// //     }

// //     @BeforeMethod(alwaysRun = true)
// //     public void createDriver() {
// //         if (getDriver() == null) {
// //             WebDriver d = DriverFactory.createDriver();
// //             TL_DRIVER.set(d);
// //             System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Session Created");
// //         }
// //     }

// //     @AfterMethod(alwaysRun = true)
// //     public void quitDriver(ITestResult result) {
// //         WebDriver d = getDriver();
// //         try {
// //             if (d != null) {
// //                 d.quit();
// //                 System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Quit");
// //             }
// //         } finally {
// //             TL_DRIVER.remove();
// //         }
// //     }
// // }

// package com.shwetha.framework.base;

// import com.shwetha.framework.driver.DriverFactory;
// import com.shwetha.framework.utils.ConfigReader;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
// import org.openqa.selenium.WebDriver;
// import org.testng.ITestResult;
// import org.testng.annotations.AfterMethod;
// import org.testng.annotations.BeforeMethod;
// import org.testng.annotations.BeforeSuite;

// public class BaseTests {

//     public final Logger log = LogManager.getLogger(getClass());

//     private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

//     // already changed to public in your copy
//     public static WebDriver getDriver() {
//         return TL_DRIVER.get();
//     }

//     // NEW: guard that creates/attaches a driver if the thread doesn't have one yet
//     public static synchronized WebDriver ensureDriver() {
//         WebDriver d = TL_DRIVER.get();
//         if (d == null) {
//             d = com.shwetha.framework.driver.DriverFactory.createDriver();
//             TL_DRIVER.set(d);
//             System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Session Created [ensure]");
//         }
//         return d;
//     }

//     @BeforeSuite(alwaysRun = true)
//     public void setUpSuite() { ConfigReader.load(); }

//     @BeforeMethod(alwaysRun = true)
//     public void createDriver() {
//         if (getDriver() == null) {
//             WebDriver d = com.shwetha.framework.driver.DriverFactory.createDriver();
//             TL_DRIVER.set(d);
//             System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Session Created");
//         }
//     }

//     @AfterMethod(alwaysRun = true)
//     public void quitDriver(ITestResult result) {
//         WebDriver d = getDriver();
//         try {
//             if (d != null) {
//                 d.quit();
//                 System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Quit");
//             }
//         } finally {
//             TL_DRIVER.remove();
//         }
//     }
// }

package com.shwetha.framework.base;

import com.shwetha.framework.driver.DriverFactory;
import com.shwetha.framework.utils.ConfigReader;
import com.shwetha.framework.utils.DataRepo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTests {

    public final Logger log = LogManager.getLogger(getClass());

    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return TL_DRIVER.get();
    }

    // somewhere during bootstrap (e.g., in BaseTests.setUpSuite())
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                com.shwetha.framework.driver.DriverRegistry.killAll();
            } catch (Throwable ignore) {
            }
        }, "driver-shutdown-hook"));
    }

    // NEW: guard creator (used by BasePage)
    public static synchronized WebDriver ensureDriver() {
        WebDriver d = TL_DRIVER.get();
        if (d == null) {
            d = DriverFactory.createDriver();
            TL_DRIVER.set(d);
            if (isEffectivelyParallel()) {
                System.out
                        .println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Session Created [ensure]");
            }
        }
        return d;
    }

    // @BeforeSuite(alwaysRun = true)
    // public void setUpSuite() { ConfigReader.load(); }

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        ConfigReader.load();

        DataRepo.publishChoice(); // announce JSON/EXCEL now that config is loaded

        boolean parallelEnabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
        boolean dpParallel = Boolean.parseBoolean(ConfigReader.get("dp.parallel", "false"));
        String suiteParallel = ConfigReader.get("suite.parallel", "none");
        String suiteThreads = ConfigReader.get("suite.thread.count", "1");
        String dpThreads = ConfigReader.get("suite.dp.thread.count", "1");
        log.info("=====================================================");
        log.info(" 🔧 TEST SUITE PARALLEL CONFIGURATION ");
        log.info("-----------------------------------------------------");
        log.info(" • Parallel Enabled          : " + parallelEnabled);
        log.info(" • DataProvider Parallel     : " + dpParallel);
        if (parallelEnabled) {
            log.info(" • Suite Parallel Mode       : " + suiteParallel);
            log.info(" • Suite Thread Count        : " + suiteThreads);
            log.info(" • DataProvider Thread Count : " + dpThreads);
        }
        log.info("=====================================================");

    }

    @BeforeMethod(alwaysRun = true)
    public void createDriver() {
        if (isEffectivelyParallel()) {
            ConfigReader.load();
            boolean parallelEnabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
            boolean dpParallel = Boolean.parseBoolean(ConfigReader.get("dp.parallel", "false"));
            String suiteParallel = ConfigReader.get("suite.parallel", "none");
            log.info("-----------------------------------------------------");
            log.info(" 🧵 THREAD " + Thread.currentThread().getId() + " STARTING TEST");
            log.info(" • Parallel Enabled     : " + parallelEnabled);
            log.info(" • Suite Parallel Mode  : " + suiteParallel);
            log.info(" • DP Parallel          : " + dpParallel);
            log.info("-----------------------------------------------------");
        }

        if (getDriver() == null) {
            WebDriver d = DriverFactory.createDriver();
            TL_DRIVER.set(d);
            if (isEffectivelyParallel()) {
                System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Session Created");
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    public void quitDriver(ITestResult result) {
        WebDriver d = getDriver();
        try {
            if (d != null) {
                try {
                    d.manage().deleteAllCookies();
                } catch (Throwable ignore) {
                }
                try {
                    d.quit();
                } catch (Throwable ignore) {
                } // don’t let exceptions block cleanup
                // d.quit();

                com.shwetha.framework.driver.DriverRegistry.unregister(d);

                if (isEffectivelyParallel()) {
                    System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Quit");
                }
            }
        } finally {
            TL_DRIVER.remove(); // critical in parallel
        }
    }

    // Helper methods
    protected void logParallelStatus() {
        log.info("Running in parallel: " + getEffectiveMode()
                + " | suite.parallel=" + getSuiteMode()
                + " | threads=" + getEffectiveSuiteThreads());
    }

    /**
     * Returns true only if either suite threads or DP threads are > 1 after
     * SuiteConfigurer applied.
     */
    public static boolean isEffectivelyParallel() {
        ConfigReader.load();
        // int tc = parseIntOr(System.getProperty("suite.effective.thread.count", "1"),
        // 1);
        // int dp = parseIntOr(System.getProperty("suite.effective.dp.thread.count",
        // "1"), 1);
        // return (tc > 1) || (dp > 1);
        // or
        // return getEffectiveSuiteThreads() > 1 || getEffectiveDpThreads() > 1;
        // or
        // if(getEffectiveMode().equalsIgnoreCase("PARALLEL")){
        // return true;
        // }
        // else{
        // return false;
        // }
        // or
        // return Boolean.parseBoolean("true");
        // or
        // return Boolean.valueOf("true");
        // or
        return Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
    }

    public static String getEffectiveMode() {
        // return System.getProperty("suite.effective.parallel.mode", "NONE");
        // or
        ConfigReader.load();
        boolean parallelEnabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
        // boolean dpParallel = Boolean.parseBoolean(ConfigReader.get("dp.parallel",
        // "false"));
        if (parallelEnabled) {
            return "PARALLEL";
        } else {
            return "NONE";
        }
    }

    public static String getSuiteMode() {
        ConfigReader.load();
        String suiteParallel = ConfigReader.get("suite.parallel", "none");
        return suiteParallel;
    }

    public static int getEffectiveSuiteThreads() {
        ConfigReader.load();
        String suiteThreads = ConfigReader.get("suite.thread.count", "1");
        return Integer.parseInt(suiteThreads);
    }

    public static int getEffectiveDpThreads() {
        ConfigReader.load();
        String dpThreads = ConfigReader.get("suite.dp.thread.count", "1");
        return Integer.parseInt(dpThreads);
    }

    private static int parseIntOr(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}
