
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