package com.automation.utils;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {
    private final WebDriver driver;
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    public ScreenshotUtil(WebDriver driver) {
        this.driver = driver;
        new File(SCREENSHOT_DIR).mkdirs();
    }

    public void captureStep(String stepName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = stepName + "_" + timestamp + ".png";
            File destination = new File(SCREENSHOT_DIR + fileName);
            FileUtils.copyFile(source, destination);
            // Attach to Allure
            try (InputStream is = Files.newInputStream(Paths.get(destination.getPath()))) {
                Allure.addAttachment(stepName, "image/png", is, ".png");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}