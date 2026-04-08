package com.automation.pages;

import com.automation.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class GoogleHomePage extends BasePage {

    @FindBy(name = "q")
    private WebElement searchBox;

    @FindBy(name = "btnK")
    private WebElement searchButton;

    @FindBy(css = "h3")
    private WebElement firstResult;

    public GoogleHomePage(WebDriver driver) {
        super(driver);
    }

    public void enterSearchText(String text) {
        sendKeys(searchBox, text, "Google Search Box");
    }

    public void clickSearchButton() {
        click(searchButton, "Google Search Button");
    }

    public String getFirstResultText() {
        wait.until(ExpectedConditions.visibilityOf(firstResult));
        return firstResult.getText();
    }

    public void searchFor(String text) {
        enterSearchText(text);
        searchBox.sendKeys(Keys.ENTER);
    }

    public boolean isResultsPageLoaded(String expectedText) {
        return wait.until(ExpectedConditions.or(
                ExpectedConditions.titleContains(expectedText),
                ExpectedConditions.visibilityOf(firstResult)
        )) != null;
    }
}
