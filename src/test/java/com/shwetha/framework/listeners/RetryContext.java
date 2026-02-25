
// src/test/java/com/shwetha/framework/listeners/RetryContext.java
package com.shwetha.framework.listeners;

import org.testng.ITestResult;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

// Tracks retry signals between RetryAnalyzer and TestListener.
public class RetryContext {
    // Marks the NEXT invocation as retry n (1 => attempt #2)
    private static final ConcurrentHashMap<String, Integer> NEXT_ATTEMPT = new ConcurrentHashMap<>();
    // Flags that the CURRENT failure will be retried (so listener should skip screenshot)
    private static final ConcurrentHashMap<String, Boolean> WILL_RETRY_NOW = new ConcurrentHashMap<>();

    private static String key(ITestResult r) {
        return r.getMethod().getQualifiedName() + "|" + Arrays.toString(r.getParameters());
    }

    public static void markRetryNextAttempt(ITestResult r, int retryAttemptNumber) {
        NEXT_ATTEMPT.put(key(r), retryAttemptNumber);
    }

    public static Integer consumeRetryNextAttemptIfAny(ITestResult r) {
        return NEXT_ATTEMPT.remove(key(r));
    }

    public static void markWillRetryNow(ITestResult r) {
        WILL_RETRY_NOW.put(key(r), Boolean.TRUE);
    }

    public static boolean consumeWillRetryNow(ITestResult r) {
        Boolean v = WILL_RETRY_NOW.remove(key(r));
        return v != null && v;
    }

    // ==== NEW: peek without consuming (used by failure handler) ====
    public static boolean isWillRetryNow(ITestResult r) {
        Boolean v = WILL_RETRY_NOW.get(key(r));
        return v != null && v;
    }
}