package com.shwetha.framework.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    public static String takeScreenshot(WebDriver driver, String baseDir, String testName) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String filePath = baseDir + File.separator + testName + "_" + timestamp + ".png";
        try {
            FileUtils.copyFile(src, new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed saving screenshot: " + filePath, e);
        }
        return filePath;
    }
}


