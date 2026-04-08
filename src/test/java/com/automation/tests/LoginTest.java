package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.GoogleHomePage;
import com.automation.utils.ConfigReader;
import com.automation.utils.ExcelDataProvider;
import com.automation.utils.JsonDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void loginWithExcelData(Map<String, String> testData) {
        executeSearchScenario(testData);
    }

    @Test(dataProvider = "jsonData", dataProviderClass = JsonDataProvider.class)
    public void loginWithJsonData(Map<String, String> testData) {
        executeSearchScenario(testData);
    }

    private void executeSearchScenario(Map<String, String> testData) {
        String searchTerm = getValue(testData, "searchTerm", "username");
        String expectedKeyword = getValue(testData, "expectedKeyword", "password");

        driver.get(ConfigReader.getUrl());
        GoogleHomePage googlePage = new GoogleHomePage(driver);
        googlePage.searchFor(searchTerm);

        Assert.assertTrue(googlePage.isResultsPageLoaded(searchTerm),
                "Google results page did not load for: " + searchTerm);

        if (expectedKeyword != null && !expectedKeyword.isEmpty()) {
            Assert.assertTrue(driver.getTitle().contains(expectedKeyword) || driver.getPageSource().contains(expectedKeyword),
                    "Expected keyword not found in results: " + expectedKeyword);
        }
    }

    private String getValue(Map<String, String> testData, String primaryKey, String fallbackKey) {
        String value = testData.get(primaryKey);
        if (value == null || value.isEmpty()) {
            value = testData.get(fallbackKey);
        }
        return value;
    }
}
