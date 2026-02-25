package com.shwetha.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

// import com.shwetha.framework.utils.WaitUtils;

import io.netty.handler.timeout.TimeoutException;

public class HomePage extends BasePage {
    private By loginLink = By.className("ico-login");
    private By logoutLink = By.className("ico-logout");
    private By searchBox  = By.id("small-searchterms");
    private By searchBtn  = By.cssSelector("input[type='submit'][value='Search']");
    private By advSearchBtn  = By.cssSelector("input.button-1.search-button");
    private By advToggle      = By.id("As");                 // Advanced search checkbox
    private By categorySelect = By.name("Cid");              // Category dropdown

    public HomePage(WebDriver driver) { 
        super(driver); 
    }

    public HomePage goTo(String baseUrl) {
        driver.get(baseUrl);
        waitForPageToLoad(); 
        return this;
    }

    public LoginPage clickLogin() { 
        clickable(loginLink).click(); 
        return new LoginPage(driver); 
    }

    // public LoginPage clickLogin() {
    //     // 1) Wait for header link to be clickable (handles stale/overlay)
    //     WaitUtils.safeClick(driver, loginLink, 10);

    //     // 2) Wait the login page to actually load
    //     //    Use either URL contains "/login" OR the presence of #Email field
    //     try {
    //         WaitUtils.waitUntilUrlContains(driver, "/login", 10);
    //     } catch (TimeoutException ignore) {
    //         // fallback: wait for field
    //         WaitUtils.waitVisible(driver, By.id("Email"), 10);
    //     }
    //     return new LoginPage(driver);
    // }

    public boolean isLoggedIn() { 
        try { 
            return driver.findElement(logoutLink).isDisplayed(); 
        } catch (Exception e) { 
            return false; 
        } 
    }

    public HomePage logoutIfLoggedIn() { 
        if (isLoggedIn()) clickable(logoutLink).click(); 
        return this; 
    }

    // public HomePage logoutIfLoggedIn() {
    //     try {
    //         // if logout visible, click it safely
    //         driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(0)); // avoid implicit waits conflicting
    //         if (!driver.findElements(logoutLink).isEmpty()) {
    //             WaitUtils.safeClick(driver, logoutLink, 8);
    //             // wait until login link returns
    //             // WaitUtils.waitVisible(driver, By.className("ico-login"), 8);
    //             WaitUtils.waitVisible(driver, loginLink, 8);
    //         }

    //         // for (int i = 0; i < 2; i++) {
    //         //     try {
    //         //         WaitUtils.waitClickable(driver, loginLink, 8);
    //         //         WaitUtils.safeClick(driver, loginLink, 8);
    //         //         break;
    //         //     } catch (TimeoutException | StaleElementReferenceException e) {
    //         //         if (i == 1) throw e;
    //         //         // optional: small pause or re-find fields; do not spam sleeps
    //         //     }
    //         // }
    //     } catch (Exception ignored) {
    //         // keep method non-fatal for tests
    //     } finally {
    //         // restore implicit if you use it globally; ideally keep 0 and rely on explicit waits
    //     }
    //     return this;
    // }

    public String title() { 
        return driver.getTitle(); 
    }

    public SearchResultsPage search(String keyword) {
        visible(searchBox).clear();
        driver.findElement(searchBox).sendKeys(keyword);
        clickable(searchBtn).click();
        waitForPageToLoad();
        return new SearchResultsPage(driver);
    }

    // // Utility to read value from an input if present
    // private String getInputValue(By by) {
    //     try { 
    //         return driver.findElement(by).getAttribute("value"); 
    //     }
    //     catch (NoSuchElementException e) { return ""; }
    // }

    public SearchResultsPage applyCategoryFilter(String containsOption) {
        // 1) Ensure Advanced is on
        WebElement adv = wait.until(ExpectedConditions.presenceOfElementLocated(advToggle));
        if (!adv.isSelected()) {
            adv.click();
        }
        // 2) Select category from dropdown
        // Select dropDownSelect = new Select(driver.findElement(By.name("Cid")));
        // dropDownSelect.selectByVisibleText(category);
        Select dropDownSelect = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(categorySelect)));
        boolean selected = false;
        for (WebElement opt : dropDownSelect.getOptions()) {
            if (opt.getText().trim().contains(containsOption)) {
                dropDownSelect.selectByVisibleText(opt.getText());
                selected = true;
                break;
            }
        }
        if (!selected) {
            // fallback: choose Electronics >> Cell phones if present
            for (WebElement opt : dropDownSelect.getOptions()) {
                if (opt.getText().toLowerCase().contains("cell phones")) {
                    dropDownSelect.selectByVisibleText(opt.getText());
                    break;
                }
            }
        }
        // 3) Click Search on the advanced panel ((advanced form has specific button with id 'advs')
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(advSearchBtn));
        btn.click();
        // driver.findElement(By.xpath("/html/body/div[4]/div[1]/div[4]/div[2]/div/div[2]/div[1]/form/div[3]/input")).click();
        waitForPageToLoad();
        // we are on the home page, and after clicking on search, it will go to search result page.
        return new SearchResultsPage(driver);
    }
}




// package com.shwetha.pageobjects;

// import java.util.List;

// import org.openqa.selenium.By;
// import org.openqa.selenium.StaleElementReferenceException;
// import org.openqa.selenium.TimeoutException;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.Select;

// import com.shwetha.framework.utils.WaitUtils;

// public class HomePage extends BasePage {
//     private By loginLink = By.className("ico-login");
//     private By logoutLink = By.className("ico-logout");
//     private By searchBox  = By.id("small-searchterms");
//     private By searchBtn  = By.cssSelector("input[type='submit'][value='Search']");
//     private By advSearchBtn  = By.cssSelector("input.button-1.search-button");
//     private By advToggle      = By.id("As");                 // Advanced search checkbox
//     private By categorySelect = By.name("Cid");              // Category dropdown

//     public HomePage(WebDriver driver) { 
//         super(driver); 
//     }

//     public HomePage goTo(String baseUrl) {
//         driver.get(baseUrl);
//         waitForPageToLoad(); 
//         return this;
//     }

//     public LoginPage clickLogin() { 
//         // waitForPageToLoad();
//         clickable(loginLink).click(); 
//         return new LoginPage(driver); 
//     }

//     // public LoginPage clickLogin() {
//     //     // 1) Wait for header link to be clickable (handles stale/overlay)
//     //     WaitUtils.safeClick(driver, loginLink, 12);

//     //     // 2) Wait the login page to actually load
//     //     //    Use either URL contains "/login" OR the presence of #Email field
//     //     try {
//     //         WaitUtils.waitUntilUrlContains(driver, "/login", 10);
//     //     } catch (TimeoutException ignore) {
//     //         // fallback: wait for field
//     //         WaitUtils.waitVisible(driver, By.id("Email"), 10);
//     //     }
//     //     return new LoginPage(driver);
//     // }

//     public boolean isLoggedIn() { 
//         try { 
//             return driver.findElement(logoutLink).isDisplayed(); 
//         } catch (Exception e) { 
//             return false; 
//         } 
//     }

//     // public HomePage logoutIfLoggedIn() { 
//     //     if (isLoggedIn()) clickable(logoutLink).click(); 
//     //     return this; 
//     // }

//     public HomePage logoutIfLoggedIn() {
//         try {
//             // if logout visible, click it safely
//             driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(0)); // avoid implicit waits conflicting
//             if (!driver.findElements(logoutLink).isEmpty()) {
//                 WaitUtils.safeClick(driver, logoutLink, 10);
//                 // wait until login link returns
//                 WaitUtils.waitVisible(driver, loginLink, 10);
//                 WaitUtils.waitClickable(driver, loginLink, 10);
//             }

//             // for (int i = 0; i < 2; i++) {
//             //     try {
//             //         WaitUtils.waitClickable(driver, loginLink, 8);
//             //         WaitUtils.safeClick(driver, loginLink, 8);
//             //         break;
//             //     } catch (TimeoutException | StaleElementReferenceException e) {
//             //         if (i == 1) throw e;
//             //         // optional: small pause or re-find fields; do not spam sleeps
//             //     }
//             // }
//         } catch (Exception ignored) {
//             // keep method non-fatal for tests
//         } finally {
//             // restore implicit if you use it globally; ideally keep 0 and rely on explicit waits
//         }
//         return this;
//     }

//     public String title() { 
//         return driver.getTitle(); 
//     }

//     public SearchResultsPage search(String keyword) {
//         WaitUtils.waitVisible(driver, searchBox, 10).clear();
//         driver.findElement(searchBox).sendKeys(keyword);
//         WaitUtils.waitClickable(driver, searchBtn, 10);
//         WaitUtils.safeClick(driver, searchBtn, 10);
//         waitForPageToLoad();
//         return new SearchResultsPage(driver);
//     }

//     // // Utility to read value from an input if present
//     // private String getInputValue(By by) {
//     //     try { 
//     //         return driver.findElement(by).getAttribute("value"); 
//     //     }
//     //     catch (NoSuchElementException e) { return ""; }
//     // }

//     public SearchResultsPage applyCategoryFilter(String containsOption) {
//         // 1) Ensure Advanced is on
//         WebElement adv = wait.until(ExpectedConditions.presenceOfElementLocated(advToggle));
//         if (!adv.isSelected()) {
//             adv.click();
//         }
//         // 2) Select category from dropdown
//         // Select dropDownSelect = new Select(driver.findElement(By.name("Cid")));
//         // dropDownSelect.selectByVisibleText(category);
//         Select dropDownSelect = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(categorySelect)));
//         boolean selected = false;
//         for (WebElement opt : dropDownSelect.getOptions()) {
//             if (opt.getText().trim().contains(containsOption)) {
//                 dropDownSelect.selectByVisibleText(opt.getText());
//                 selected = true;
//                 break;
//             }
//         }
//         if (!selected) {
//             // fallback: choose Electronics >> Cell phones if present
//             for (WebElement opt : dropDownSelect.getOptions()) {
//                 if (opt.getText().toLowerCase().contains("cell phones")) {
//                     dropDownSelect.selectByVisibleText(opt.getText());
//                     break;
//                 }
//             }
//         }
//         // 3) Click Search on the advanced panel ((advanced form has specific button with id 'advs')
//         WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(advSearchBtn));
//         btn.click();
//         // driver.findElement(By.xpath("/html/body/div[4]/div[1]/div[4]/div[2]/div/div[2]/div[1]/form/div[3]/input")).click();
//         waitForPageToLoad();
//         // we are on the home page, and after clicking on search, it will go to search result page.
//         return new SearchResultsPage(driver);
//     }
// }














