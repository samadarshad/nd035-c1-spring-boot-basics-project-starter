package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.WebDriverWaitTimeoutSeconds;

public class SignupPage {
    @FindBy(id = "inputFirstName")
    public WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    public WebElement inputLastName;

    @FindBy(id = "inputUsername")
    public WebElement inputUsername;

    @FindBy(id = "inputPassword")
    public WebElement inputPassword;

    @FindBy(id = "submit-button")
    public WebElement submitButton;

    @FindBy(id = "success")
    public WebElement successMsg;

    @FindBy(id = "error")
    public WebElement errorMsg;

    public SignupPage (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void waitForPage(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds).until(ExpectedConditions.elementToBeClickable(submitButton));
    }

    public void signup(String firstname, String lastname, String username, String password) {
        inputFirstName.sendKeys(firstname);
        inputLastName.sendKeys(lastname);
        inputUsername.sendKeys(username);
        inputPassword.sendKeys(password);
        submitButton.click();
    }
}
