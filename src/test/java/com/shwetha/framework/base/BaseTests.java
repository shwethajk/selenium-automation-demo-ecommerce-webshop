

// src/test/java/com/shwetha/framework/base/BaseTests.java

package com.shwetha.framework.base;

import com.shwetha.framework.driver.DriverFactory;
import com.shwetha.framework.utils.ConfigReader;
import com.shwetha.framework.utils.DataRepo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

public class BaseTests {

    public final Logger log = LogManager.getLogger(getClass());

    /** 
     * We support BOTH modes and pick at runtime:
     *  - ThreadLocal drivers (for CI / parallel)
     *  - Single shared driver (for local lightweight runs)
     */
    private static final ThreadLocal<WebDriver> TL_DRIVER = new ThreadLocal<>();
    private static volatile WebDriver SHARED_DRIVER = null;

    /** Expose driver to tests (unchanged signature). */
    public static WebDriver getDriver() { 
        return useThreadLocal() ? TL_DRIVER.get() : SHARED_DRIVER; 
    }

    // Register a shutdown hook (unchanged behavior)
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { com.shwetha.framework.driver.DriverRegistry.killAll(); } catch (Throwable ignore) {}
        }, "driver-shutdown-hook"));
    }

    /** Ensure a driver exists for the current mode. */
    public static synchronized WebDriver ensureDriver() {
        WebDriver d = getDriver();
        if (d == null) {
            d = DriverFactory.createDriver();
            setDriver(d);
            if (isEffectivelyParallel()) {
                System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Session Created [ensure]");
            } else {
                System.out.println("LOCAL -> Shared WebDriver Session Created [ensure]");
            }
        }
        return d;
    }

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        ConfigReader.load();

        DataRepo.publishChoice(); // announce JSON/EXCEL now that config is loaded

        boolean parallelEnabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
        boolean dpParallel      = Boolean.parseBoolean(ConfigReader.get("dp.parallel", "false"));
        String suiteParallel    = ConfigReader.get("suite.parallel", "none");
        String suiteThreads     = ConfigReader.get("suite.thread.count", "1");
        String dpThreads        = ConfigReader.get("suite.dp.thread.count", "1");

        log.info("=====================================================");
        log.info(" 🔧 TEST SUITE PARALLEL CONFIGURATION ");
        log.info("-----------------------------------------------------");
        log.info(" • Driver Mode               : " + (useThreadLocal() ? "THREAD-LOCAL (CI/parallel)" : "SINGLE (local)"));
        log.info(" • Parallel Enabled          : " + parallelEnabled);
        log.info(" • DataProvider Parallel     : " + dpParallel);
        if (parallelEnabled){
            log.info(" • Suite Parallel Mode       : " + suiteParallel);
            log.info(" • Suite Thread Count        : " + suiteThreads);
            log.info(" • DataProvider Thread Count : " + dpThreads);
        }
        if(useThreadLocal()){
            log.info("Using threadLocal driver (CI/Parallel)");
            // org.testng.Reporter.log("Using threadLocal driver (CI/Parallel)\n", true);
            System.out.println("Using threadLocal driver (CI/Parallel)");
        }
        else{
            log.info("Using shared local driver");
            System.out.println("Using shared local driver");
        }
        log.info("=====================================================");
    }

    @BeforeMethod(alwaysRun = true)
    public void createDriver() {
        if (isEffectivelyParallel()){
            ConfigReader.load();
            boolean parallelEnabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
            boolean dpParallel      = Boolean.parseBoolean(ConfigReader.get("dp.parallel", "false"));
            String suiteParallel    = ConfigReader.get("suite.parallel", "none");
            log.info("-----------------------------------------------------");
            log.info(" 🧵 THREAD " + Thread.currentThread().getId() + " STARTING TEST");
            log.info(" • Parallel Enabled     : " + parallelEnabled);
            log.info(" • Suite Parallel Mode  : " + suiteParallel);
            log.info(" • DP Parallel          : " + dpParallel);
            log.info("-----------------------------------------------------");
        }

        if (getDriver() == null) {
            WebDriver d = DriverFactory.createDriver();
            setDriver(d);
        }
        // In local single-driver mode, we sanitize session between tests
        if (!useThreadLocal()) {
            try { getDriver().manage().deleteAllCookies(); } catch (Throwable ignore) {}
        }
        System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Created");
    }

    @AfterMethod(alwaysRun = true)
    public void quitDriver(ITestResult result) {
        if (useThreadLocal()) {
            // Parallel/CI mode: quit per test method (existing behavior)
            WebDriver d = getDriver();
            try {
                if (d != null) {
                    try { d.manage().deleteAllCookies(); } catch (Throwable ignore) {}
                    try { d.quit(); } catch (Throwable ignore) {} // don't block cleanup
                    com.shwetha.framework.driver.DriverRegistry.unregister(d);
                    System.out.println("THREAD " + Thread.currentThread().getId() + " -> WebDriver Quit");
                }
            } finally {
                TL_DRIVER.remove(); // critical in parallel
            }
        } else {
            // Local mode: keep shared driver alive; just clean cookies to isolate tests
            WebDriver d = getDriver();
            if (d != null) {
                try { d.manage().deleteAllCookies(); } catch (Throwable ignore) {}
                // NOTE: do NOT quit here; we quit once at @AfterSuite.
                System.out.println("LOCAL -> Reusing Shared WebDriver (cleaned cookies)");
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        // Only relevant for local single-driver mode
        if (!useThreadLocal()) {
            WebDriver d = SHARED_DRIVER;
            SHARED_DRIVER = null;
            try {
                if (d != null) {
                    try { d.manage().deleteAllCookies(); } catch (Throwable ignore) {}
                    try { d.quit(); } catch (Throwable ignore) {}
                    com.shwetha.framework.driver.DriverRegistry.unregister(d);
                    System.out.println("LOCAL -> Shared WebDriver Quit @AfterSuite");
                }
            } finally {
                // nothing else
            }
        }
    }

    // ===== Helper methods (mode, config, etc.) =====

    /** Returns true only if we should run with ThreadLocal drivers (CI or parallel). */
    public static boolean useThreadLocal() {
        // Priority:
        //  0) If it's a CI environment -> ThreadLocal
        //  1) If not CI and explicitely mentioned as 'noThreadMode' -> local
        //  2) Else if parallel.enabled=true -> ThreadLocal
        //  3) Else -> single shared driver (local)

        if (isCiEnvironment()) return true;

        boolean noThreadMode = Boolean.parseBoolean(ConfigReader.get("noThreadMode", "true"));
        if(noThreadMode && !isCiEnvironment()) {
            return false;
        }
        else{
            return true;
        }
        // return isEffectivelyParallel();
    }

    /** Detects common CI environments so you don’t have to toggle anything manually. */
    private static boolean isCiEnvironment() {
        Map<String, String> env = System.getenv();
        return "true".equalsIgnoreCase(env.getOrDefault("CI", "false"))
                || env.containsKey("JENKINS_HOME")
                || "true".equalsIgnoreCase(env.getOrDefault("GITHUB_ACTIONS", "false"))
                || "true".equalsIgnoreCase(env.getOrDefault("GITLAB_CI", "false"))
                || env.containsKey("BUILD_NUMBER")
                || env.containsKey("TEAMCITY_VERSION")
                || env.containsKey("BITBUCKET_BUILD_NUMBER")
                || env.containsKey("AZURE_HTTP_USER_AGENT");
    }

    /** Returns true only if either suite or DP config requests parallel runs. */
    public static boolean isEffectivelyParallel() {
        ConfigReader.load();
        return Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
    }

    public static String getEffectiveMode() {
        ConfigReader.load();
        boolean parallelEnabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
        return parallelEnabled ? "PARALLEL" : "NONE";
    }

    public static String getSuiteMode() {
        ConfigReader.load();
        return ConfigReader.get("suite.parallel", "none");
    }

    public static int getEffectiveSuiteThreads() {
        ConfigReader.load();
        return Integer.parseInt(ConfigReader.get("suite.thread.count", "1"));
    }

    public static int getEffectiveDpThreads() {
        ConfigReader.load();
        return Integer.parseInt(ConfigReader.get("suite.dp.thread.count", "1"));
    }

    // Internal setters to centralize driver storage by mode
    private static void setDriver(WebDriver d) {
        if (useThreadLocal()) {
            TL_DRIVER.set(d);
        } else {
            SHARED_DRIVER = d;
        }
    }
}




