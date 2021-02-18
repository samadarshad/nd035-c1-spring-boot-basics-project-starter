package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.click;

public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "signup-link")
    private WebElement signupLink;

    public LoginPage (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void login(WebDriver driver, String username, String password) {
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        click(driver, submitButton);
    }

    public void goToSignup() {
        signupLink.click();
    }
}