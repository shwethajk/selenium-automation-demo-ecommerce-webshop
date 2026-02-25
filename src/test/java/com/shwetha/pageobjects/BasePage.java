
package com.shwetha.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.Properties;

// import org.testng.annotations.BeforeClass;
// import org.openqa.selenium.support.PageFactory;
// import java.io.File;
// import java.io.IOException;
// import java.text.SimpleDateFormat;
// import org.apache.commons.lang3.RandomStringUtils;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    public Properties p;   // property file object
    // @BeforeClass(groups= {"Sanity","Regression","Master"})
	// @Parameters({"os","browser"})

    // protected BasePage(WebDriver driver) {
    //     this.driver = driver;
    //     this.wait  = new WebDriverWait(driver, Duration.ofSeconds(10));
    //     // 		PageFactory.initElements(driver,this);
    // }


    // protected BasePage(WebDriver driver) {
    //     // Fallback to the thread's driver if a null was accidentally passed
    //     this.driver = (driver != null) ? driver : com.shwetha.framework.base.BaseTests.getDriver();
    //     if (this.driver == null) {
    //         throw new IllegalStateException(
    //             "WebDriver is not initialized. Ensure BaseTests.createDriver() runs before page object creation."
    //         );
    //     }
    //     this.wait  = new WebDriverWait(this.driver, Duration.ofSeconds(10));
    // }

    protected BasePage(WebDriver driver) {
        this.driver = (driver != null)
            ? driver
            : com.shwetha.framework.base.BaseTests.ensureDriver();
        this.wait  = new WebDriverWait(this.driver, Duration.ofSeconds(10));
    }

    protected void waitForPageToLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(
            wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

    protected WebElement clickable(By by) { 
        return wait.until(ExpectedConditions.elementToBeClickable(by)); 
    }

    protected WebElement visible(By by)   { 
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by)); 
    }

    protected boolean isPresent(By by) {
        try { driver.findElement(by); return true; } catch (NoSuchElementException e) { return false; }
    }

    protected void scrollIntoView(WebElement el) {
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    // public String randomeString(){
	// 	String generatedstring=RandomStringUtils.randomAlphabetic(5);
	// 	return generatedstring;
	// }
	// public String randomeNumber(){
	// 	String generatednumber=RandomStringUtils.randomNumeric(10);
	// 	return generatednumber;
	// }
	// public String randomeAlphaNumberic(){
	// 	String generatedstring=RandomStringUtils.randomAlphabetic(3);
	// 	String generatednumber=RandomStringUtils.randomNumeric(3);
	// 	return (generatedstring+"@"+generatednumber);
	// }
	// public String captureScreen(String tname) throws IOException {
	// 	String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());	
	// 	TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
	// 	File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
	// 	String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
	// 	File targetFile=new File(targetFilePath);
	// 	sourceFile.renameTo(targetFile);
	// 	return targetFilePath;
	// }
	
}