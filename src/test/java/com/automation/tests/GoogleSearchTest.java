package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.GoogleHomePage;
import com.automation.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleSearchTest extends BaseTest {

    @Test
    public void testGoogleSearch() {
        driver.get(ConfigReader.getUrl());
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        googlePage.searchFor("Selenium WebDriver");
        Assert.assertTrue(googlePage.getFirstResultText().contains("Selenium"),
                "First result does not contain 'Selenium'");
    }
}