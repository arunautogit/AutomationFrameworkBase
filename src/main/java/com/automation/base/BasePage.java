package com.automation.base;

import com.automation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected Logger log;
    protected ScreenshotUtil screenshotUtil;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        this.log = LogManager.getLogger(this.getClass());
        this.screenshotUtil = new ScreenshotUtil(driver);
        PageFactory.initElements(driver, this);
    }

    // Click with retry, logging, screenshot
    protected void click(WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            log.info("Clicked on: " + elementName);
            screenshotUtil.captureStep("PASS_click_" + elementName);
        } catch (Exception e) {
            log.error("Failed to click on: " + elementName, e);
            screenshotUtil.captureStep("FAIL_click_" + elementName);
            throw e;
        }
    }

    protected void sendKeys(WebElement element, String text, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            element.clear();
            element.sendKeys(text);
            log.info("Entered text '" + text + "' into: " + elementName);
            screenshotUtil.captureStep("PASS_sendKeys_" + elementName);
        } catch (Exception e) {
            log.error("Failed to send keys to: " + elementName, e);
            screenshotUtil.captureStep("FAIL_sendKeys_" + elementName);
            throw e;
        }
    }

    protected void scrollToElement(WebElement element, String elementName) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            log.info("Scrolled to: " + elementName);
            screenshotUtil.captureStep("PASS_scroll_" + elementName);
        } catch (Exception e) {
            log.error("Failed to scroll to: " + elementName, e);
            screenshotUtil.captureStep("FAIL_scroll_" + elementName);
            throw e;
        }
    }

    protected void hover(WebElement element, String elementName) {
        try {
            actions.moveToElement(element).perform();
            log.info("Hovered on: " + elementName);
            screenshotUtil.captureStep("PASS_hover_" + elementName);
        } catch (Exception e) {
            log.error("Failed to hover on: " + elementName, e);
            screenshotUtil.captureStep("FAIL_hover_" + elementName);
            throw e;
        }
    }

    protected void switchToWindow(String windowTitle) {
        String originalWindow = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(windowTitle)) {
                log.info("Switched to window: " + windowTitle);
                screenshotUtil.captureStep("PASS_switchWindow_" + windowTitle);
                return;
            }
        }
        driver.switchTo().window(originalWindow);
        log.warn("Window with title " + windowTitle + " not found");
        screenshotUtil.captureStep("FAIL_switchWindow_" + windowTitle);
    }
}