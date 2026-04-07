package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.SamplePage;
import com.automation.utils.ConfigReader;
import com.automation.utils.ExcelDataProvider;
import com.automation.utils.JsonDataProvider;
import org.testng.annotations.Test;
import java.util.Map;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
    public void loginWithExcelData(Map<String, String> testData) {
        driver.get(ConfigReader.getUrl());
        SamplePage samplePage = new SamplePage(driver);
        samplePage.login(testData.get("username"), testData.get("password"));
        // Add assertions
    }

    @Test(dataProvider = "jsonData", dataProviderClass = JsonDataProvider.class)
    public void loginWithJsonData(Map<String, String> testData) {
        driver.get(ConfigReader.getUrl());
        SamplePage samplePage = new SamplePage(driver);
        samplePage.login(testData.get("username"), testData.get("password"));
    }
}