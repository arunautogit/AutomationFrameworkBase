package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.GoogleHomePage;
import com.automation.utils.ConfigReader;
import com.automation.utils.JsonDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;

public class GoogleSearchTest extends BaseTest {

    @Test(dataProvider = "jsonData", dataProviderClass = JsonDataProvider.class)
    public void testGoogleSearchDataDriven(Map<String, String> testData) {
        driver.get(ConfigReader.getUrl());
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        googlePage.searchFor(testData.get("searchTerm"));
        Assert.assertTrue(googlePage.getFirstResultText().contains(testData.get("expectedKeyword")),
                "First result does not contain expected keyword: " + testData.get("expectedKeyword"));
    }
}