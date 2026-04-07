package com.automation.base;

import com.automation.drivers.DriverFactory;
import com.automation.listeners.AlertListener;
import com.automation.listeners.ExtentReportListener;
import com.automation.listeners.RetryListener;
import com.automation.listeners.VideoListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners({ExtentReportListener.class, VideoListener.class, AlertListener.class, RetryListener.class})
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initDriver();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
        driver = null;
    }
}