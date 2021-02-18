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

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(xpath = "//*[@id=\"fileTable\"]/tbody/tr/th")
    private List<WebElement> fileNameList;

    @FindBy(xpath = "//*[@id=\"fileTable\"]/tbody/tr/td/a")
    private List<WebElement> fileDownloadLinkList;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-notes']/div[1]/table/tbody/tr/th")
    private List<WebElement> noteTitleList;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-notes']/div[1]/table/tbody/tr/td[2]")
    private List<WebElement> noteDescriptionList;

    @FindBy(id = "nav-files-tab")
    public WebElement navFilesTab;

    @FindBy(id = "nav-notes-tab")
    public WebElement navNotesTab;

    @FindBy(id = "nav-credentials-tab")
    public WebElement navCredentialsTab;

    public HomePage (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public Boolean isLoggedIn() {
        return logoutButton != null;
    }

    public List<String> getFileNameList() {
        return fileNameList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void waitForLogin(WebDriver driver) throws InterruptedException {
        //wait-until isnt working as intended, so just using a delay
        Thread.sleep(500);
//        new WebDriverWait(driver, 3000)
//                .until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab")));
//                .until(d -> d.findElement(By.id("nav-notes-tab")));
//                .until(ExpectedConditions.elementToBeClickable(navNotesTab));
    }

    public void waitForNotes(WebDriver driver) throws InterruptedException {
        //wait-until isnt working as intended, so just using a delay
        Thread.sleep(500);
//        new WebDriverWait(driver, 3000)
//                .until(ExpectedConditions.visibilityOfAllElements(noteTitleList));
    }

    public List<String> getNoteTitleList() {
        return noteTitleList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void logout() {
        logoutButton.click();
    }
}