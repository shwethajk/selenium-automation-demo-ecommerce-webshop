/*
package com.shwetha.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.Select;
import java.util.List;

public class SearchResultsPage extends BasePage {
    private By resultItems     = By.cssSelector(".product-item");
    private By resultTitleAnch = By.cssSelector(".product-title a");

    public SearchResultsPage(WebDriver driver) { 
        super(driver); 
    }

    public boolean hasAnyMatch(String keyword) {
        try {
            List<WebElement> items = wait.until(
                ExpectedConditions.refreshed(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(resultItems)));
            for (WebElement item : items) {
                try {
                    if (item.getText().toLowerCase().contains(keyword.toLowerCase())) return true;
                } catch (StaleElementReferenceException ignore) {}
            }
        } catch (Exception ignored) {}
        return false;
    }

    public ProductPage openProduct(String exactName) {
        List<WebElement> links = driver.findElements(resultTitleAnch);
        for (WebElement l : links) {
            try {
                if (l.getText().equalsIgnoreCase(exactName)) {
                    scrollIntoView(l); l.click(); waitForPageToLoad();
                    return new ProductPage(driver);
                }
            }
            catch (StaleElementReferenceException ignore) {}
        }
        return null;
    }
}
*/


// package com.shwetha.pageobjects;

// import org.openqa.selenium.*;
// import org.openqa.selenium.support.ui.ExpectedCondition;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.FluentWait;

// // import org.openqa.selenium.support.ui.Select;
// import java.util.List;
// import java.util.Locale;
// import java.time.Duration;



// public class SearchResultsPage extends BasePage {
//     private By resultItems     = By.cssSelector(".product-item");
//     private By resultTitleAnch = By.cssSelector(".product-title a");

//     public SearchResultsPage(WebDriver driver) { 
//         super(driver); 
//     }

//     // public boolean hasAnyMatch(String keyword) {
//     //     try {
//     //         List<WebElement> items = wait.until(
//     //             ExpectedConditions.refreshed(
//     //                 ExpectedConditions.presenceOfAllElementsLocatedBy(resultItems)));
//     //         for (WebElement item : items) {
//     //             try {
//     //                 if (item.getText().toLowerCase().contains(keyword.toLowerCase())) return true;
//     //             } catch (StaleElementReferenceException ignore) {}
//     //         }
//     //     } catch (Exception ignored) {}
//     //     return false;
//     // }

//     /**
//      * More robust "found" check:
//      *  1) Fast-path: poll the page body text for the query (case-insensitive).
//      *  2) Optional path: if you have a stable results container, wait for it to be non-empty.
//      *
//      * No dependency on specific list item selectors → less flakiness in CI/headless/grid.
//      */
//     public boolean hasAnyMatch(String queryRaw) {
//         final String query = (queryRaw == null ? "" : queryRaw).trim().toLowerCase(Locale.ROOT);
//         if (query.isBlank()) return false;

//         FluentWait<WebDriver> wait = new FluentWait<>(driver)
//                 .withTimeout(Duration.ofSeconds(15))     // total budget
//                 .pollingEvery(Duration.ofMillis(300))    // nimble polling
//                 .ignoring(NoSuchElementException.class)
//                 .ignoring(StaleElementReferenceException.class)
//                 .ignoring(JavascriptException.class);

//         // 1) JS fallback: check body text contains the query (robust across layouts)
//         ExpectedCondition<Boolean> bodyTextContainsQuery = drv -> {
//             Object r = ((JavascriptExecutor) drv).executeScript(
//                     "var b = document.body; " +
//                     "if(!b) return false; " +
//                     "var t = (b.innerText || '').toLowerCase(); " +
//                     "return t.indexOf(arguments[0]) >= 0;",
//                     query
//             );
//             return (r instanceof Boolean) ? (Boolean) r : Boolean.FALSE;
//         };

//         // 2) (Optional) If you have a stable results container, uncomment and include:
//         // ExpectedCondition<Boolean> containerHasItems = drv -> {
//         //     try {
//         //         WebElement c = drv.findElement(RESULTS_CONTAINER);
//         //         String txt = (c.getText() == null ? "" : c.getText()).toLowerCase(Locale.ROOT);
//         //         return txt.contains(query);
//         //     } catch (Throwable ignore) {
//         //         return false;
//         //     }
//         // };

//         // Try body text first (most resilient)
//         Boolean ok = wait.until(bodyTextContainsQuery);
//         // If you also want to chain the container check, you could AND/OR it here.
//         return ok != null && ok;
//     }


//     public ProductPage openProduct(String exactName) {
//         List<WebElement> links = driver.findElements(resultTitleAnch);
//         for (WebElement l : links) {
//             try {
//                 if (l.getText().equalsIgnoreCase(exactName)) {
//                     scrollIntoView(l); l.click(); waitForPageToLoad();
//                     return new ProductPage(driver);
//                 }
//             }
//             catch (StaleElementReferenceException ignore) {}
//         }
//         return null;
//     }
// }



// src/test/java/com/shwetha/pageobjects/SearchResultsPage.java
package com.shwetha.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class SearchResultsPage extends BasePage {
    private final By resultItems     = By.cssSelector(".product-item");
    private final By resultTitleAnch = By.cssSelector(".product-title a");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    /** NEW: Wait until results are in a terminal state (items visible OR a no-results message appears). */
    private void waitForResultsReady() {
        FluentWait<WebDriver> w = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(JavascriptException.class);

        w.until(drv -> {
            // 1) DOM ready
            try {
                Object rs = ((JavascriptExecutor) drv).executeScript("return document.readyState");
                if (!"complete".equals(String.valueOf(rs))) return false;
            } catch (Throwable ignore) {}

            // 2) If items are present -> ready
            try {
                List<WebElement> items = drv.findElements(resultItems);
                if (items != null && !items.isEmpty()) return true;
            } catch (Throwable ignore) {}

            // 3) Or a typical "no results" text (generic, layout-agnostic)
            try {
                String body = String.valueOf(((JavascriptExecutor) drv)
                        .executeScript("return (document.body && document.body.innerText) || ''"))
                        .toLowerCase(Locale.ROOT);
                if (body.contains("no results")
                        || body.contains("no products")
                        || body.contains("no product matches")
                        || body.contains("0 results")) {
                    return true;
                }
            } catch (Throwable ignore) {}

            return false; // keep polling
        });
    }

    /**
     * Robust "found" check that works for both positive and negative cases.
     * - Returns true once the body text contains the query (case-insensitive).
     * - Returns false if the wait expires (NO exception leaked to tests).
     */
    public boolean hasAnyMatch(String queryRaw) {
        final String query = (queryRaw == null ? "" : queryRaw).trim().toLowerCase(Locale.ROOT);
        if (query.isBlank()) return false;

        // ensure page/results settled (prevents headless/grid jitter)
        waitForResultsReady();

        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(JavascriptException.class);

        // JS body-text contains query (layout/theme agnostic)
        ExpectedCondition<Boolean> bodyTextContainsQuery = drv -> {
            Object r = ((JavascriptExecutor) drv).executeScript(
                    "var b = document.body; if(!b) return false; " +
                    "var t = (b.innerText || '').toLowerCase(); " +
                    "return t.indexOf(arguments[0]) >= 0;", query);
            return (r instanceof Boolean) ? (Boolean) r : Boolean.FALSE;
        };

        try {
            Boolean ok = wait.until(bodyTextContainsQuery);
            return ok != null && ok;
        } catch (TimeoutException te) {
            // IMPORTANT: for negative cases, we return false instead of throwing
            return false;
        }
    }

    public ProductPage openProduct(String exactName) {
        // Small hardening: wait for anchor presence before scanning
        try {
            new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(8))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(resultTitleAnch));
        } catch (TimeoutException ignore) {
            // fall through to scan whatever is present (keeps behavior minimal)
        }

        List<WebElement> links = driver.findElements(resultTitleAnch);
        for (WebElement l : links) {
            try {
                if (l.getText().equalsIgnoreCase(exactName)) {
                    scrollIntoView(l);
                    l.click();
                    waitForPageToLoad();
                    return new ProductPage(driver);
                }
            } catch (StaleElementReferenceException ignore) {}
        }
        return null;
    }
}
