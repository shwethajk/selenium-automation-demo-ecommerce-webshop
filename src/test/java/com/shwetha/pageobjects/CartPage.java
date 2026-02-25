
package com.shwetha.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class CartPage extends BasePage {
    private By cartRows       = By.cssSelector(".cart-item-row");
    private By removeCheckbox = By.name("removefromcart");
    private By updateBtn      = By.name("updatecart");

    public CartPage(WebDriver driver) { 
        super(driver); 
    }

    public boolean containsProduct(String productName) {
        for (WebElement r : driver.findElements(cartRows)) {
            if (r.getText().toLowerCase().contains(productName.toLowerCase())) return true;
        }
        return false;
    }

    public CartPage removeProduct(String productName) {
        List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cartRows));
        for (WebElement r : rows) {
            try {
                if (r.getText().toLowerCase().contains(productName.toLowerCase())) {
                    r.findElement(removeCheckbox).click();
                    driver.findElement(updateBtn).click();
                    wait.until(ExpectedConditions.stalenessOf(r));
                    break;
                }
            } catch (StaleElementReferenceException e) {
                return removeProduct(productName);
            }
        }
        return this;
    }
}