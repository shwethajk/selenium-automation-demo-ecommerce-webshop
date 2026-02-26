// src\test\java\com\shwetha\framework\listeners\TestListener.java

package com.shwetha.framework.listeners;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.shwetha.framework.base.BaseTests;
import com.shwetha.framework.utils.ConfigReader;
import com.shwetha.framework.utils.ScreenshotUtils;
import org.testng.*;

//Extent report 5.x...//version
import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

// ==== ADDED for data-index + safe rename ====
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// import java.net.URL;
// import java.nio.file.Paths;
// import java.util.List;
// import org.apache.commons.mail.DefaultAuthenticator;
// import org.apache.commons.mail.ImageHtmlEmail;
// import org.apache.commons.mail.resolver.DataSourceUrlResolver;
// import org.apache.xmlbeans.impl.xb.xsdschema.Attribute.Use;
// import org.testng.xml.XmlSuite;
// import com.google.common.collect.Tables;
// import com.aventstack.extentreports.markuputils.MarkupHelper;

public class TestListener extends BaseTests implements ITestListener, ISuiteListener {
    public ExtentSparkReporter sparkReporter;
    public ExtentReports extent;
    public ExtentTest test;
    String repName;

    // ==== NEW: Stable data index per (method + parameters) and node registry for
    // retro-rename ====
    private static final ConcurrentHashMap<String, AtomicInteger> METHOD_SEQ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> METHOD_PARAM_INDEX = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<Integer, ExtentTest>> METHOD_INDEX_NODE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>> METHOD_INDEX_TCID = new ConcurrentHashMap<>();

    private static String methodKey(ITestResult r) {
        return r.getMethod().getQualifiedName();
    }

    private static String paramKey(Object[] params) {
        return java.util.Arrays.deepToString(params == null ? new Object[0] : params);
    }

    /**
     * Returns the same data index for the same (method+parameters) and a new index
     * for first-time rows. Retries reuse the same index.
     */
    private static int stableDataIndex(ITestResult r) {
        String mk = methodKey(r);
        String pk = paramKey(r.getParameters());
        ConcurrentHashMap<String, Integer> map = METHOD_PARAM_INDEX.computeIfAbsent(mk, k -> new ConcurrentHashMap<>());
        Integer existing = map.get(pk);
        if (existing != null)
            return existing;
        int next = METHOD_SEQ.computeIfAbsent(mk, k -> new AtomicInteger(0)).incrementAndGet();
        map.put(pk, next);
        return next;
    }

    @Override
    public void onStart(ISuite suite) {

        // ✅ Load configuration BEFORE any other logic that needs it
        // try { com.shwetha.framework.utils.ConfigReader.load(); }
        // catch (Throwable t) { System.out.println("[WARN] Config load failed: " +
        // t.getMessage()); }

        String msg = "\n\n***********************************************************************\n ==== Suite START: {} ==== ";
        org.testng.Reporter.log(
                msg + suite.getName() + "\n***********************************************************************\n",
                true); // true => also log to console
        log.info(msg, suite.getName());

        // 👇 Announce data source: excel/json (console + log + set system props)
        // com.shwetha.framework.utils.DataRepo.publishChoice();

        // log the effective mode (parallel/not)
        String effMode = getEffectiveMode();
        String suiteLevel = getSuiteMode();
        int effTC = getEffectiveSuiteThreads();
        int effDP = getEffectiveDpThreads();
        String headlessNess = ConfigReader.get("headless", "false");
        if(effMode.equalsIgnoreCase("Parallel"))
            Reporter.log(String.format(
                "🧭 EFFECTIVE PARALLEL -> mode = %s, suite-level = %s, threadCount = %d, dpThreadCount = %d, headless = %s\n",
                effMode, suiteLevel, effTC, effDP, headlessNess), true);
        else
            Reporter.log(String.format("🧭 EFFECTIVE PARALLEL -> mode = %s, headless = %s\n", effMode, headlessNess), true);
    }

    @Override
    public void onStart(ITestContext testContext) {
        /*
         * SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
         * Date dt=new Date();
         * String currentdatetimestamp=df.format(dt);
         */
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        // repName = "Extent2-Test-Report-" + ".html"; // you can also suffix timestamp
        // if you want
        repName = "ExtentTest-Report-" + timeStamp + ".html";
      //  sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName); // specify location of the report


        sparkReporter = new ExtentSparkReporter("reports/" + repName); // specify location of the report
        
        // Compute absolute report path safely (for showing screenshot on report)
        reportPath = Paths.get(System.getProperty("user.dir"), "reports", repName).toAbsolutePath().normalize();

        sparkReporter.config().setDocumentTitle("Demowebshop Automation Report"); // Title of report
        sparkReporter.config().setReportName("Functional Test Results"); // name of the report
        sparkReporter.config().setTheme(Theme.DARK); // LIGHT

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // ---------- Collect run configuration ----------
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "false").trim());
        String runMode = ConfigReader.get("run.mode", "local").trim(); // local | grid
        String browser = ConfigReader.get("browser", "chrome").trim();
        String effMode = getEffectiveMode();
        String effTC = Integer.toString(getEffectiveSuiteThreads());
        String effDP = Integer.toString(getEffectiveDpThreads());
        // Optional OS/Browser from testng params if you pass them
        String osParam = safe(testContext.getCurrentXmlTest().getParameter("os"));
        String browserParam = safe(testContext.getCurrentXmlTest().getParameter("browser"));
        // ---------- Put key/value pairs in Extent's System Info sidebar ----------
        extent.setSystemInfo("Application", "opencart");
        extent.setSystemInfo("Environemnt", "QA");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Sub Module", "Customers");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("", "");
        extent.setSystemInfo("Run Mode", runMode); // local / grid
        extent.setSystemInfo("Operating System", osParam.isBlank() ? System.getProperty("os.name") : osParam);
        extent.setSystemInfo("Browser", browserParam.isBlank() ? browser : browserParam);
        extent.setSystemInfo("Headless", String.valueOf(headless));
        extent.setSystemInfo("Parallel Mode", effMode);
        extent.setSystemInfo("Suite Threads", effTC);
        extent.setSystemInfo("DP Threads", effDP);

        // ---------- Also add a clear "Run Summary" section ON the landing page
        // ----------
        // Create a summary test node pinned to the top (prettier table)
        ExtentTest summary = extent.createTest("🏁 Run Summary");
        // Use MarkupHelper.createTable (cleanest: Tables render perfectly in Spark and
        // keep things aligned)
        String[][] envTable = new String[][] {
                { "Run Mode", runMode },
                { "Browser", browserParam.isBlank() ? browser : browserParam },
                { "Headless", String.valueOf(headless) }
        };
        String[][] parTable = new String[][] {
                { "Effective Mode", effMode },
                { "Suite Threads", String.valueOf(effTC) },
                { "DataProvider Threads", String.valueOf(effDP) }
        };
        String[][] hostTable = new String[][] {
                { "OS", osParam.isBlank() ? System.getProperty("os.name") : osParam },
                { "Java", System.getProperty("java.version") },
                { "User", System.getProperty("user.name") }
        };
        summary.info("<b>Environment</b>");
        summary.info(MarkupHelper.createTable(envTable));
        summary.info("<b>Parallel</b>");
        summary.info(MarkupHelper.createTable(parTable));
        summary.info("<b>Host</b>");
        summary.info(MarkupHelper.createTable(hostTable));
        // (Existing group/module info if any)
        var includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
            summary.info("**Included Groups:** " + includedGroups);
        }
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    @Override
    public void onTestStart(ITestResult result) {
        // --- Build context ---
        Object[] params = result.getParameters(); // Build a small parameters string for readability
        String paramStr = (params == null || params.length == 0)
                ? ""
                : " | params=" + java.util.Arrays.toString(params);

        // Detect retry (from RetryContext set by RetryAnalyzer)
        Integer retryAttempt = com.shwetha.framework.listeners.RetryContext.consumeRetryNextAttemptIfAny(result);
        int attempt = (retryAttempt == null) ? 0 : retryAttempt; // 0 = first run, 1 = first retry, ...

        // --- Compute stable [data #n] for this (method + params). Retries reuse the
        // same index. ---
        int dataIndex = stableDataIndex(result);
        result.setAttribute("dataIndex", dataIndex);
        result.setAttribute("attemptNumber", attempt);

        // Optional TC_ID (for Map-based providers) — show even if only one row
        String tcId = null;
        if (params != null && params.length > 0 && params[0] instanceof java.util.Map<?, ?> m) {
            Object tc = ((java.util.Map<?, ?>) m).get("TC_ID");
            if (tc != null && String.valueOf(tc).trim().length() > 0) {
                tcId = String.valueOf(tc).trim();
            }
        }
        String mk = methodKey(result);
        String baseName = result.getMethod().getMethodName();

        // ---- Register this invocation's node storage keys up-front ---- // (node
        // itself is created below; we also cache TC_ID per index for later
        // retro-rename)
        METHOD_INDEX_NODE.computeIfAbsent(mk, k -> new ConcurrentHashMap<>());
        METHOD_INDEX_TCID.computeIfAbsent(mk, k -> new ConcurrentHashMap<>()).put(dataIndex, tcId == null ? "" : tcId);

        // Decide bracket visibility using the ACTUAL number of distinct rows observed
        // so far // Important: distinct count must be checked from METHOD_PARAM_INDEX
        // *after* stableDataIndex() created/updated it.
        int distinctCount = METHOD_PARAM_INDEX.getOrDefault(mk, new ConcurrentHashMap<>()).size();
        boolean showBracket = (distinctCount >= 2) || (tcId != null); // <-- DO NOT show bracket for single-row retry

        String bracket = showBracket
                ? " [data " + dataIndex + (tcId == null ? "" : " - " + tcId) + "]"
                : "";
        String retrySuffix = (attempt > 0) ? " (Retry)" : "";
        String startName = baseName + bracket + retrySuffix; // --- CREATE/NAME the Extent node with FINAL base at start
                                                             // ---

        test = extent.createTest(startName).assignCategory(result.getMethod().getGroups()); // to display groups in
                                                                                            // badges in report
        // bind this invocation's node so other callbacks operate on the right node
        result.setAttribute("extentNode", test);
        METHOD_INDEX_NODE.get(mk).put(dataIndex, test);

        // If this is the FIRST time we reached exactly 2 distinct rows for this method,
        // retro-rename the *first* node to append "[data 1]" and TC_ID (if any).
        if (distinctCount == 2) {
            ExtentTest firstNode = METHOD_INDEX_NODE.getOrDefault(mk, new ConcurrentHashMap<>()).get(1);
            if (firstNode != null) {
                String firstName = firstNode.getModel().getName();
                if (!firstName.matches(".*\\[\\s*data\\s*1.*\\].*")) { // avoid double appending
                    String firstTcId = METHOD_INDEX_TCID.getOrDefault(mk, new ConcurrentHashMap<>()).getOrDefault(1,
                            "");
                    String suffix = firstTcId.isEmpty() ? "" : " - " + firstTcId;
                    String renamed = firstName + " [data 1" + suffix + "]";
                    renameNode(firstNode, renamed);
                    Reporter.log("ℹ Renamed: " + renamed, true);
                }
            }
        }
        String banner = "\n -----------------------------------------------------------------------\n";
        String prefix = (attempt > 0) ? ("\n▶  START (RETRY) : ") : (banner + "▶  START : ");
        String msg = prefix + startName + paramStr; // <-- log the same final name
        Reporter.log(msg, true); // true => also log to console
        log.info(msg);

        String desc = result.getMethod().getDescription(); // Description (if present) as an Info step
        if (desc != null && !desc.isBlank()) {
            test.info("Description: " + desc);
        }
        if (params != null && params.length > 0) { // Parameters (if present)
            test.info("Parameters: " + java.util.Arrays.toString(params));
        }
        if (attempt > 0) { // Retry marker as an info badge
            test.info("Retry attempt: " + attempt);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest node = (ExtentTest) result.getAttribute("extentNode");
        if (node == null)
            node = test; // fallback

        Integer att = (Integer) result.getAttribute("attemptNumber");
        String currentName = node.getModel().getName();
        if (att != null && att > 0) {
            // rename first, then log PASS once (no duplicates)
            String renamed = currentName.replaceFirst("\\s*\\(Retry\\)\\s*$", "")
                    .replaceFirst("\\s*\\(Retry\\s*-\\w+\\)\\s*$", "");
            renamed += " (Retry - Pass)";
            renameNode(node, renamed);
            Reporter.log("✅  PASS  : " + renamed, true);
            node.log(Status.PASS, renamed + " got successfully executed");
            log.info("✅  PASS  : " + renamed);
        } else {
            Reporter.log("✅  PASS  : " + currentName, true);
            node.log(Status.PASS, currentName + " got successfully executed");
            log.info("✅  PASS  : " + currentName);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest node = (ExtentTest) result.getAttribute("extentNode");
        if (node == null)
            node = test; // fallback

        Throwable t = result.getThrowable();
        String currentName = node.getModel().getName();

        // Screenshot only on FINAL failure (no retry pending)
        boolean willRetry = com.shwetha.framework.listeners.RetryContext.isWillRetryNow(result); // <--- peek
        if (!willRetry) {
            try {
                String shotsDir = ConfigReader.get("screenshots.dir", "target/screenshots");
                String path = ScreenshotUtils.takeScreenshot(BaseTests.getDriver(), shotsDir,
                        result.getMethod().getMethodName());
                Reporter.log("📷  Screenshot: " + path, true);
                String relPath = toReportRelative(path); // <-- key line: find the relative path using the raw path
                node.addScreenCaptureFromPath(relPath); // <-- attach relative path
            } catch (Throwable e) {
                Reporter.log("Screenshot capture failed", true);
                node.log(Status.WARNING,
                        result.getMethod().getMethodName() + " : Screenshot capture failed: " + e.getMessage());
                log.warn("Screenshot capture failed", e);
            }
        } else {
            Reporter.log("↩  Skipping screenshot (retry scheduled)", true);
            node.log(Status.INFO, "↩  Skipping screenshot (retry scheduled)");
            log.warn("↩  Skipping screenshot (retry scheduled)");
        }
        // log + report
        String msg = "❌  FAIL  : " + currentName +
                " | reason = " + (t != null ? t.getMessage() : "n/a");
        Reporter.log(msg, true);
        log.error(msg);
        if (t != null)
            node.log(Status.FAIL, t); // Write failure details

        // rename to Retry - Fail only when this was a retry attempt AND no further
        // retry is pending (final)
        Integer att = (Integer) result.getAttribute("attemptNumber");
        if (att != null && att > 0 && !willRetry) {
            String renamed = currentName.replaceFirst("\\s*\\(Retry\\)\\s*$", "")
                    .replaceFirst("\\s*\\(Retry\\s*-\\w+\\)\\s*$", "");
            renamed += " (Retry - Fail)";
            renameNode(node, renamed);
            Reporter.log("ℹ Renamed: " + renamed, true);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest node = (ExtentTest) result.getAttribute("extentNode");
        if (node == null)
            node = test; // fallback
        String currentName = node.getModel().getName();
        String msg = "⏭  SKIP : " + currentName
                + (result.getThrowable() != null ? " | reason=" + result.getThrowable().getMessage() : "");
        Reporter.log(msg, true);
        log.warn(msg);
        // (Optional) Screenshot on skip: Screenshot only if driver exists
        try {
            String shotsDir = ConfigReader.get("screenshots.dir", "target/screenshots");
            if (com.shwetha.framework.base.BaseTests.getDriver() != null) {
                String path = com.shwetha.framework.utils.ScreenshotUtils
                        .takeScreenshot(com.shwetha.framework.base.BaseTests.getDriver(), shotsDir,
                                result.getMethod().getMethodName());
                Reporter.log("📷 Skip Screenshot: " + path, true);
                String relPath = toReportRelative(path); // <-- key line: find the relative path using the raw path
                node.addScreenCaptureFromPath(relPath); // <-- attach relative path to report
            } else {
                Reporter.log("📷 Skip Screenshot: driver not available (skipped before driver init)", true);
                node.log(Status.INFO, "Skip screenshot not captured (no driver).");
                // log.warn("Skip Screenshot capture failed", e);
            }
        } catch (Throwable e) {
            Reporter.log("Skip Screenshot capture failed", true);
            node.log(Status.WARNING,
                    result.getMethod().getMethodName() + " : Skip Screenshot capture failed: " + e.getMessage());
            log.warn("Skip Screenshot capture failed", e);
        }

        // If this skip is due to retry flow, rename to "(Retry scheduled)" and consume
        // the flag here
        boolean scheduled = com.shwetha.framework.listeners.RetryContext.consumeWillRetryNow(result);
        if (scheduled) {
            String renamed = currentName.replaceFirst("\\s*\\(Retry\\)\\s*$", "")
                    .replaceFirst("\\s*\\(Retry\\s*-\\w+\\)\\s*$", "");
            renamed += " (Retry scheduled)";
            renameNode(node, renamed);
            Reporter.log("ℹ Renamed: " + renamed, true);
        }

        if (result.getThrowable() != null) { // Log skip details in Extent
            node.log(Status.SKIP, result.getThrowable());
            node.log(Status.INFO, result.getThrowable().getMessage());
        } else {
            node.log(Status.SKIP, result.getMethod().getMethodName() + " got skipped");
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        String msg = "\n***********************************************************************\n";
        log.info("\n ==== Suite END: {} ==== " + suite.getName() + msg);
        org.testng.Reporter.log("\n ==== Suite END: {} ==== " + suite.getName() + msg, true); // true => also log to
                                                                                              // console
        extent.flush();
       // String pathOfExtentReport = System.getProperty("user.dir") + "\\reports\\" + repName;
     //  String pathOfExtentReport = System.getProperty("user.dir") + "/reports/" + repName;
      // org.testng.Reporter.log("📷  Report saved at: " + pathOfExtentReport
                // + "\n***********************************************************************\n", true);

        String pathOfExtentReport = reportPath.toString();

org.testng.Reporter.log(
        "📷  Report saved at: " + pathOfExtentReport
                + "\n***********************************************************************\n",
        true
);
        
        
        extent.flush();

/*
        try {
            Desktop.getDesktop().browse(extentReport.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        try {
            com.shwetha.framework.driver.DriverRegistry.killAll();
        } catch (Throwable ignore) {
        } // parallel clean
    }

    // Helper functions:
    private Path reportPath; // remember the absolute path of the HTML

    private String toReportRelative(String absoluteScreenshotPath) {
        try {
            Path shot = Paths.get(absoluteScreenshotPath).toAbsolutePath().normalize();
            Path reportDir = reportPath.getParent(); // folder where HTML is written
            String rel = reportDir.relativize(shot).toString();
            return rel.replace('\\', '/'); // browsers prefer '/' even on Windows
        } catch (Exception e) {
            // fall back to absolute path (will work when the file is on the same host)
            return absoluteScreenshotPath.replace('\\', '/');
        }
    }

    /**
     * Try to rename node after outcome (retry-pass/fail). No impact if Extent
     * internals change.
     */
    private static void renameNode(com.aventstack.extentreports.ExtentTest t, String name) {
        try {
            if (t != null && t.getModel() != null)
                t.getModel().setName(name);
        } catch (Throwable ignore) {
        }
    }

    /** Utility: open file if present; print a helpful message otherwise */
    private void openIfExists(Desktop desktop, Path file, String label) {
        try {
            if (Files.exists(file)) {
                desktop.browse(file.toUri()); // browse is more tolerant than open on Windows
                System.out.println("[OPEN] " + label + " report -> " + file.toAbsolutePath());
            } else {
                System.out.println("[SKIP] " + label + " not found -> " + file.toAbsolutePath());
            }
        } catch (IOException ioe) {
            System.out.println("[WARN] Could not open " + label + ": " + ioe.getMessage() +
                    " | path: " + file.toAbsolutePath());
        }
    }
}
