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
        return "grid".equals(mode) ? createRemote(headless) : createLocalWithRetry();
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

    private static WebDriver createLocalWithRetry() {
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
                ChromeOptions options = baseOptions(false); // do NOT set headless here
                // Decide headless flavor per attempt
                boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "false").trim());
                if (headless) {
                    if (attempts < 3) {
                        options.addArguments("--headless=new"); // default on attempts 1 & 2
                    } else {
                        options.addArguments("--headless"); // legacy fallback on attempt 3
                    }
                    options.addArguments("--window-size=1920,1080");
                }
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
                    options.addArguments("--disable-gpu", "--disable-software-rasterizer",
                            "--disable-extensions", "--disable-background-networking");
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

                d.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
                d.manage().window().maximize();
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

    private static ChromeOptions baseOptions(boolean ignoredHeadlessFlag) {
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--remote-debugging-port=0");
        options.addArguments("--no-first-run", "--no-default-browser-check");
        return options;
    }

    // private static ChromeDriverService buildSilentService(String driverExe) {
    // .usingDriverExecutable(new File(driverExe))

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
}