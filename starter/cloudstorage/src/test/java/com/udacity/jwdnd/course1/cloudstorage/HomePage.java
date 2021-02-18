package com.udacity.jwdnd.course1.cloudstorage;

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

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-files']/div/table/tbody/tr/th")
    private List<WebElement> fileNameList;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-notes']/div[1]/table/tbody/tr/th")
    private List<WebElement> noteTitleList;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-notes']/div[1]/table/tbody/tr/td[2]")
    private List<WebElement> noteDescriptionList;

    @FindBy(id = "nav-files-tab")
    public WebElement navFilesTab;

    @FindBy(id = "nav-notes-tab")
    public WebElement navNotesTab;

    @FindBy(id = "nav-notes")
    public WebElement navNotes;

    public HomePage (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public Boolean isLoggedIn() {
        return logoutButton != null;
    }

    public List<String> getFileNameList() {
        return fileNameList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void waitForNotes(WebDriver driver) {
        new WebDriverWait(driver, 1000)
                .until(ExpectedConditions.visibilityOfAllElements(noteTitleList));
    }


    public List<String> getNoteTitleList() {
        return noteTitleList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void logout() {
        logoutButton.click();
    }
}