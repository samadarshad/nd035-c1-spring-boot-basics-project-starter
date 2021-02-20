package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.*;

public class LoginPage {
    @FindBy(id = "inputUsername")
    public WebElement inputUsername;

    @FindBy(id = "inputPassword")
    public WebElement inputPassword;

    @FindBy(id = "submit-button")
    public WebElement submitButton;

    @FindBy(id = "signup-link")
    public WebElement signupLink;

    @FindBy(id = "error")
    public WebElement errorMsg;

    @FindBy(id = "logout")
    public WebElement logoutMsg;

    public LoginPage (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void waitForLoginPage(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds).until(ExpectedConditions.elementToBeClickable(submitButton));
    }

    public void login(WebDriver driver, String username, String password) {
        sendKeys(driver, inputUsername, username);
        sendKeys(driver, inputPassword, password);
        click(driver, submitButton);
    }

    public void goToSignup() {
        signupLink.click();
    }
}