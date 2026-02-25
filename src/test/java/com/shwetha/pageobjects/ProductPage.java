

package com.shwetha.pageobjects;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {
    // private By addToCartBtn = By.cssSelector("input[value='Add to cart']"); // this would select the recommended products add to cart button
    private By addToCartBtn = By.cssSelector(
        "div.product-essential input.button-1.add-to-cart-button:not(.product-box-add-to-cart-button)"
    );
    // // or
    // private By addToCartBtn = By.cssSelector(
    //     "div.product-essential input[id^='add-to-cart-button-']:not(.product-box-add-to-cart-button)"
    // );
    private By notifBar     = By.id("bar-notification");
    private By notifClose   = By.cssSelector("#bar-notification .close");
    private By headerCart   = By.className("cart-label");

    public ProductPage(WebDriver driver) { 
        super(driver); 
    }

    public boolean hasAddToCartButton() { 
        return isPresent(addToCartBtn); 
        //  try {
        //         WebElement r = root();
        //         return !r.findElements(addToCartBtn).isEmpty()
        //                && r.findElement(addToCartBtn).isDisplayed();
        //     } catch (NoSuchElementException | TimeoutException e) {
        //         return false;
        //     }

    }

    public ProductPage addToCart() {
        try { WebElement btn = clickable(addToCartBtn); scrollIntoView(btn); btn.click(); } catch (Exception ignore) {}
        return this;
    }

    public boolean addedToCartSuccessfully() {
        try {
            WebElement bar = wait.until(ExpectedConditions.visibilityOfElementLocated(notifBar));
            boolean ok = (bar.getAttribute("class") + "").toLowerCase().contains("success");
            try { clickable(notifClose).click(); } catch (Exception ignore) {}
            return ok;
        } catch (Exception e) { return false; }
    }

    public CartPage openCartFromHeader() {
        driver.findElement(headerCart).click(); waitForPageToLoad();
        return new CartPage(driver);
    }
}