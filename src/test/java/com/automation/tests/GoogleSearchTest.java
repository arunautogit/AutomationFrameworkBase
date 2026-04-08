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
        Assert.assertTrue(googlePage.isResultsPageLoaded("Selenium WebDriver"),
                "Google results page did not load for the query");
    }
}
