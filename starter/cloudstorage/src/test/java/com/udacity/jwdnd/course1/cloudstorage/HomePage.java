package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    public HomePage (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public Boolean isLoggedIn() {
        return logoutButton != null;
    }

    public void logout() {
        logoutButton.click();
    }
}