package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.WebDriverWaitTimeoutSeconds;

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    public HomePage (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public Boolean isLoggedIn() {
        return logoutButton != null;
    }

    public void waitForLogin(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds).until(ExpectedConditions.elementToBeClickable(logoutButton));
    }

    public void logout() {
        logoutButton.click();
    }
}