
package com.shwetha.pageobjects;

import org.openqa.selenium.*;

// import com.shwetha.framework.utils.WaitUtils;

public class LoginPage extends BasePage {
    private By emailField = By.id("Email");
    private By passwordField = By.id("Password");
    private By loginButton   = By.cssSelector("input.login-button");
    private By errorBox      = By.cssSelector(".validation-summary-errors");

    public LoginPage(WebDriver driver) { 
        super(driver); 
    }

    public HomePage loginValid(String email, String password) {
        visible(emailField).clear(); driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).clear(); driver.findElement(passwordField).sendKeys(password);
        clickable(loginButton).click(); waitForPageToLoad();
        return new HomePage(driver);
    }

    public LoginPage loginInvalid(String email, String password) {
        visible(emailField).clear(); driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).clear(); driver.findElement(passwordField).sendKeys(password);
        clickable(loginButton).click(); waitForPageToLoad();
        return this;
    }



    // public HomePage loginValid(String username, String password) {
    //     WaitUtils.waitVisible(driver, emailField, 10);         // form is present
    //     WaitUtils.typeStable(driver, emailField, username, 8); // robust fill
    //     WaitUtils.typeStable(driver, passwordField, password, 8);

    //     WaitUtils.waitClickable(driver, loginButton, 8);
    //     WaitUtils.safeClick(driver, loginButton, 8);

    //     // Optional post-condition: wait for logout icon to confirm landing
    //     try { WaitUtils.waitVisible(driver, By.className("ico-logout"), 8); } catch (Exception ignored) {}
    //     return new HomePage(driver);
    // }

    // public void loginInvalid(String username, String password) {
    //     WaitUtils.waitVisible(driver, emailField, 10);
    //     WaitUtils.typeStable(driver, emailField, username, 8);
    //     WaitUtils.typeStable(driver, passwordField, password, 8);

    //     WaitUtils.waitClickable(driver, loginButton, 8);
    //     WaitUtils.safeClick(driver, loginButton, 8);
    // }

    public boolean isErrorShown() { 
        return isPresent(errorBox); 
    }
}


