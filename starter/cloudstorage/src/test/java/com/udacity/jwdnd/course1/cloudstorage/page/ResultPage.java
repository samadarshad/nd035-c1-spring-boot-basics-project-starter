package com.udacity.jwdnd.course1.cloudstorage.page;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.*;

public class ResultPage {
    @FindBy(id = "success")
    public WebElement successMsg;

    @FindBy(id = "error")
    public WebElement errorMsg;

    public ResultPage (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void waitForPage(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds).until(
                d -> d.getTitle().equals("Result")
        );
    }


}