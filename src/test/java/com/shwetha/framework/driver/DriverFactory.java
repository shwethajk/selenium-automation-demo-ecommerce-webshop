// // // src\test\java\com\shwetha\framework\driver\DriverFactory.java

// // package com.shwetha.framework.driver;

// // import com.shwetha.framework.utils.ConfigReader;
// // import io.github.bonigarcia.wdm.WebDriverManager;
// // import org.openqa.selenium.WebDriver;
// // import org.openqa.selenium.chrome.ChromeDriver;
// // import org.openqa.selenium.chrome.ChromeOptions;
// // import org.openqa.selenium.remote.RemoteWebDriver;

// // import java.net.MalformedURLException;
// // import java.net.URL;

// // public class DriverFactory {
// //     public static WebDriver createDriver() 
// //     {
// //         String mode = ConfigReader.get("run.mode", "local").trim().toLowerCase();
// //         String browser = ConfigReader.get("browser", "chrome");
// //         boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "false"));

// //         if (!"chrome".equalsIgnoreCase(browser)) {
// //             throw new RuntimeException("Only Chrome is configured in this sample. Requested: " + browser);
// //         }

// //         ChromeOptions options = new ChromeOptions();
// //         options.setAcceptInsecureCerts(true);

// //         // Friendly defaults
// //         // options.addArguments("--disable-gpu");
// //         // options.addArguments("--no-sandbox");
// //         options.addArguments("--ignore-certificate-errors");
// //         options.addArguments("--disable-dev-shm-usage");  // avoid /dev/shm issues (harmless on Windows)
// //         options.addArguments("--remote-allow-origins=*");  // prevents origin mismatch in newer Chromes
// //         options.addArguments("--remote-debugging-port=0");   // <<< add this
// //         options.addArguments("--no-first-run");
// //         options.addArguments("--no-default-browser-check");

// //         if (headless) {
// //             options.addArguments("--headless=new");
// //             options.addArguments("--window-size=1920,1080");
// //         }

// //         // Per-thread temporary Chrome profile (fixes DevToolsActivePort under headless)
// //         try {
// //             java.nio.file.Path tmp = java.nio.file.Files.createTempDirectory("chrome-profile-" + Thread.currentThread().getId());
// //             options.addArguments("--user-data-dir=" + tmp.toAbsolutePath());  // <<< add this
// //         } catch (Exception ignore) {}

// //         // grid (parallel)
// //         if ("grid".equals(mode)) {
// //             String gridUrl = ConfigReader.get("grid.url", "http://localhost:4444/wd/hub");
// //             try {
// //                 RemoteWebDriver remote = new RemoteWebDriver(new URL(gridUrl), options);
// //                 remote.manage().window().maximize();
// //                 return remote;
// //             } catch (MalformedURLException e) {
// //                 throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
// //             }
// //         }

// //         // local
// //         String driverPath = ConfigReader.get("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
// //         if (driverPath != null && !driverPath.isBlank()) {
// //             System.setProperty("webdriver.chrome.driver", driverPath);
// //             System.out.println("Driver path set: " + driverPath);
// //         } else {
// //             try {
// //                 WebDriverManager.chromedriver().setup();
// //             } catch (Throwable t) {
// //                 // Fallback to Selenium Manager via ChromeDriver() if WebDriverManager cannot resolve
// //             }
// //         }
// //         ChromeDriver local = new ChromeDriver(options);
// //         local.manage().window().maximize();
// //         return local;
// //     }
// // }

// // src\test\java\com\shwetha\framework\driver\DriverFactory.java
// package com.shwetha.framework.driver;

// import com.shwetha.framework.utils.ConfigReader;
// import io.github.bonigarcia.wdm.WebDriverManager;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;
// import org.openqa.selenium.remote.RemoteWebDriver;

// import java.net.MalformedURLException;
// import java.net.URL;
// import java.nio.file.Files;
// import java.nio.file.Path;

// public class DriverFactory {
//     public static WebDriver createDriver() {
//         String mode = ConfigReader.get("run.mode", "local").trim().toLowerCase();
//         String browser = ConfigReader.get("browser", "chrome");
//         boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "false"));

//         if (!"chrome".equalsIgnoreCase(browser)) {
//             throw new RuntimeException("Only Chrome is configured in this sample. Requested: " + browser);
//         }

//         ChromeOptions options = new ChromeOptions();
//         options.setAcceptInsecureCerts(true);

//         // Friendly defaults on Windows
//         options.addArguments("--ignore-certificate-errors");
//         options.addArguments("--disable-dev-shm-usage");
//         options.addArguments("--remote-allow-origins=*");
//         options.addArguments("--remote-debugging-port=0");    // let Chrome choose a free port
//         options.addArguments("--no-first-run");
//         options.addArguments("--no-default-browser-check");

//         if (headless) {
//             options.addArguments("--headless=new");
//             options.addArguments("--window-size=1920,1080");
//         }

//         // Per-thread temp profile -> avoids DevToolsActivePort/profile contention
//         try {
//             Path tmp = Files.createTempDirectory("chrome-profile-" + Thread.currentThread().getId());
//             options.addArguments("--user-data-dir=" + tmp.toAbsolutePath());
//         } catch (Exception ignore) { }

//         // If your Chrome is non-standard location, allow an override via config
//         String chromeBin = ConfigReader.get("chrome.binary", "");
//         if (chromeBin != null && !chromeBin.isBlank()) {
//             options.setBinary(chromeBin);
//         }

//         if ("grid".equals(mode)) {
//             String gridUrl = ConfigReader.get("grid.url", "http://localhost:4444/wd/hub");
//             try {
//                 RemoteWebDriver remote = new RemoteWebDriver(new URL(gridUrl), options);
//                 remote.manage().window().maximize();
//                 return remote;
//             } catch (MalformedURLException e) {
//                 throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
//             }
//         }

//         // --- Local driver path (you pin this due to corporate network) ---
//         String driverPath = ConfigReader.get("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver_145_2.exe");
//         if (driverPath != null && !driverPath.isBlank()) {
//             System.setProperty("webdriver.chrome.driver", driverPath);
//             System.out.println("Driver path set: " + driverPath);
//         } else {
//             try {
//                 WebDriverManager.chromedriver().setup();
//             } catch (Throwable t) {
//                 // Fallback to Selenium Manager if WDM can't resolve
//             }
//         }

//         ChromeDriver local = new ChromeDriver(options);
//         local.manage().window().maximize();
//         return local;
//     }
// }

// src/test/java/com/shwetha/framework/driver/DriverFactory.java

package com.shwetha.framework.driver;

import com.shwetha.framework.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class DriverFactory {

    public static WebDriver createDriver() {
        String mode = ConfigReader.get("run.mode", "local").trim().toLowerCase();
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "false").trim());
        String browser = ConfigReader.get("browser", "chrome");
        if (!"chrome".equalsIgnoreCase(browser)) {
            throw new RuntimeException("Only Chrome is configured. Requested: " + browser);
        }
        // return "grid".equals(mode) ? createRemote(headless) : createLocalWithRetry();
        WebDriver d = "grid".equals(mode) ? createRemote(headless) : createLocalWithRetry(headless);
        configureTimeouts(d); // NEW: uniform timeouts/implicit wait per env
        return d;
    }

    private static WebDriver createRemote(boolean headless) {
        ChromeOptions options = baseOptions(headless);
        String gridUrl = ConfigReader.get("grid.url", "http://localhost:4444/wd/hub");
        try {
            RemoteWebDriver remote = new RemoteWebDriver(new URL(gridUrl), options);
            remote.manage().window().maximize();
            return remote;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL: " + gridUrl, e);
        }
    }

    private static WebDriver createLocalWithRetry(boolean headless) {
        // String driverPath = ConfigReader.get("webdriver.chrome.driver",
        // "C:\\chromedriver-win64\\chromedriver_145_2.exe");
        // if (driverPath != null && !driverPath.isBlank()) {
        // System.setProperty("webdriver.chrome.driver", driverPath);
        // // System.out.println("Driver path set: " + driverPath);
        // } else {
        try {
            WebDriverManager.chromedriver().setup();
        } catch (Throwable ignore) {
        }
        // }

        // quiet chromedriver and swallow its logs
        // ChromeDriverService baseService = buildSilentService(driverPath);
        ChromeDriverService baseService = buildSilentService();
        int attempts = 0;
        RuntimeException last = null;

        while (attempts < 3) {
            attempts++;
            try {
                // Build base options each attempt
                ChromeOptions options = baseOptions(headless); 
                // Decide headless flavor per attempt
                // if (headless) {
                //     if (attempts < 3) {
                //         options.addArguments("--headless=new"); // default on attempts 1 & 2
                //     } else {
                //         options.addArguments("--headless"); // legacy fallback on attempt 3
                //     }
                //     options.addArguments("--window-size=1920,1080");
                // }
                // per-attempt temp profile (isolates DevToolsActivePort/profile races)
                Path tmp = Path.of(System.getProperty("java.io.tmpdir"),
                        "cd-" + Thread.currentThread().getId() + "-" + attempts);
                try {
                    Files.createDirectories(tmp);
                } catch (Exception ignore) {
                }
                options.addArguments("--user-data-dir=" + tmp.toAbsolutePath());

                DriverRegistry.registerTempProfile(tmp.toAbsolutePath().toString()); // parallel clean

                // escalate only on retries
                if (attempts == 2) {
                    // options.addArguments("--disable-gpu", "--disable-software-rasterizer",
                            // "--disable-extensions", "--disable-background-networking");
                    options.addArguments("--disable-software-rasterizer",
                                         "--disable-extensions",
                                         "--disable-background-networking");
                } else if (attempts == 3) {
                    options.addArguments("--no-sandbox");
                }
                // use a fresh service on any free port
                // .usingDriverExecutable(new File(driverPath))

                ChromeDriverService svc = new ChromeDriverService.Builder()
                        .withSilent(true)
                        .withLogOutput(devNull())
                        .usingAnyFreePort()
                        .build();
                ChromeDriver d = new ChromeDriver(svc, options);

                com.shwetha.framework.driver.DriverRegistry.register(d); // parallel clean

                // d.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
                try { d.manage().window().maximize(); } catch (Throwable ignore) {}
                return d;
            } catch (RuntimeException ex) {
                last = ex;
                String m = String.valueOf(ex.getMessage()).toLowerCase();
                boolean startupRace = m.contains("devtoolsactiveport")
                        || m.contains("timed out waiting for driver server to start")
                        || m.contains("session not created")
                        || m.contains("browser start-up failure");
                if (!startupRace)
                    break; // different failure → no point retrying
                try {
                    Thread.sleep(600L * attempts);
                } catch (InterruptedException ignored) {
                }
            }
        }
        throw last != null ? last : new RuntimeException("Chrome did not start");
    }

    private static ChromeOptions baseOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        // options.addArguments("--ignore-certificate-errors");
        // options.addArguments("--disable-dev-shm-usage");
        // options.addArguments("--remote-allow-origins=*");
        // options.addArguments("--remote-debugging-port=0");
        // options.addArguments("--no-first-run", "--no-default-browser-check");
        
        // Core stability flags
        options.addArguments(
                "--ignore-certificate-errors",
                "--disable-dev-shm-usage",              // /dev/shm constraints in containers
                "--remote-allow-origins=*",
                "--remote-debugging-port=0",
                "--no-first-run",
                "--no-default-browser-check",
                "--disable-background-timer-throttling",
                "--disable-renderer-backgrounding",
                "--disable-features=Translate,CalculateNativeWinOcclusion",
                "--lang=en-US"
        );

        if (headless) {
            // Headless stability
            options.addArguments("--headless=new", "--window-size=1920,1080", "--disable-gpu");
            if (isCiEnvironment()) {
                options.addArguments("--no-sandbox"); // needed for many CI Linux containers
            }
        }

        // Optional override if Chrome binary is custom (kept compatible with your config approach)
        String chromeBin = ConfigReader.get("chrome.binary", "").trim();
        if (!chromeBin.isBlank()) {
            options.setBinary(chromeBin);
        }

        return options;
    }

    // private static ChromeDriverService buildSilentService(String driverExe) {
    // .usingDriverExecutable(new File(driverExe))

    /** Apply uniform timeouts and a tiny implicit wait only in CI/parallel to reduce flakiness. */
    private static void configureTimeouts(WebDriver d) {
        int pageLoad = pageLoadTimeoutSeconds();
        int script   = scriptTimeoutSeconds();
        int implicit = implicitWaitSeconds();

        try { d.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad)); } catch (Throwable ignore) {}
        try { d.manage().timeouts().scriptTimeout(Duration.ofSeconds(script)); } catch (Throwable ignore) {}
        try { d.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit)); } catch (Throwable ignore) {}
    }

    private static ChromeDriverService buildSilentService() {
        try {
            return new ChromeDriverService.Builder()
                    .withSilent(true)
                    .withLogOutput(devNull())
                    .usingAnyFreePort()
                    .build();
        } catch (Exception e) {
            return new ChromeDriverService.Builder().withSilent(true).usingAnyFreePort().build();
        }
    }

    private static FileOutputStream devNull() {
        try {
            File sink = new File(System.getProperty("os.name").toLowerCase().contains("win") ? "NUL" : "/dev/null");
            return new FileOutputStream(sink);
        } catch (Exception e) {
            return null; // fine, logs go nowhere
        }
    }

    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }

    
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

    private static String defaultHeadless() {
        // In CI, default headless=true unless explicitly overridden
        return isCiEnvironment() ? "true" : "false";
    }

    private static int implicitWaitSeconds() {
        // Tiny implicit wait ONLY for CI/parallel to absorb jitter without impacting local/dev perf
        boolean parallelEnabled = Boolean.parseBoolean(ConfigReader.get("parallel.enabled", "false"));
        boolean useCiDefaults   = isCiEnvironment() || parallelEnabled;
        if (useCiDefaults) {
            return parseIntOr(ConfigReader.get("wait.implicit.ci.seconds", "3"), 3);
        } else {
            return parseIntOr(ConfigReader.get("wait.implicit.local.seconds", "0"), 0);
        }
    }

    private static int pageLoadTimeoutSeconds() {
        return parseIntOr(ConfigReader.get("wait.pageload.seconds", "90"), 90);
    }

    private static int scriptTimeoutSeconds() {
        return parseIntOr(ConfigReader.get("wait.script.seconds", "60"), 60);
    }

    private static int parseIntOr(String s, int def) {
        try { return Integer.parseInt(String.valueOf(s).trim()); } catch (Exception e) { return def; }
    }
}
