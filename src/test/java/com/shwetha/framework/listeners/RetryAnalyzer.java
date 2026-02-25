
// // // src/test/java/com/shwetha/framework/listeners/RetryAnalyzer.java


package com.shwetha.framework.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 1; // retry once

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            // NEW: persist attempt number on the result (1 => first retry)
            RetryAnalyzer.setAttempt(result, retryCount); 
            // Log a clear RETRY line
            String classAndMethod = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
            String params = (result.getParameters() == null || result.getParameters().length == 0)
                    ? ""
                    : " | params=" + java.util.Arrays.toString(result.getParameters());
            String reason = (result.getThrowable() != null) ? result.getThrowable().getMessage() : "n/a";
            String msg = String.format("🔁 TRY (%d/%d) : %s%s | reason=%s",
                    retryCount, maxRetryCount, classAndMethod, params, reason);

            org.testng.Reporter.log(msg, true);

            // Signal listener: next start is a retry; and current failure should NOT screenshot
            RetryContext.markRetryNextAttempt(result, retryCount); // stores 1 for first retry
            RetryContext.markWillRetryNow(result);

            return true;
        }
        return false; // final failure will go to listener and will screenshot
    }

    public static int getAttempt(ITestResult result) {
        Object val = result.getAttribute("retryAttempt");
        return (val instanceof Integer) ? (int) val : 0;
    }
    public static void setAttempt(ITestResult result, int a) {
        result.setAttribute("retryAttempt", a);
    }
}


