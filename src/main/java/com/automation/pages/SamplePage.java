package com.automation.pages;

import com.automation.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SamplePage extends BasePage {
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "loginBtn")
    private WebElement loginButton;

    public SamplePage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        sendKeys(usernameField, username, "Username field");
        sendKeys(passwordField, password, "Password field");
        click(loginButton, "Login button");
    }
}