package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.WebDriverWaitTimeoutSeconds;

public class HomePageNoteTab {
    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-notes']/div[1]/table/tbody/tr/th")
    private List<WebElement> noteTitleList;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-notes']/div[1]/table/tbody/tr/td[2]")
    private List<WebElement> noteDescriptionList;

    @FindBy(id = "nav-notes-tab")
    public WebElement navNotesTab;

    @FindBy(xpath = "//*[@id=\"nav-notes\"]/button")
    public WebElement addNoteButton;

    @FindBy(xpath = "//*[@id=\"note-title\"]")
    public WebElement noteTitleInput;

    @FindBy(xpath = "//*[@id=\"note-description\"]")
    public WebElement noteDescriptionInput;

    @FindBy(id = "note-save-changes")
    public WebElement noteSaveChangesButton;

    @FindBy(id = "note-cancel-changes")
    public WebElement noteCancelChangesButton;

    public HomePageNoteTab (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void waitForNav(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.elementToBeClickable(navNotesTab));
    }

    public void waitForNotes(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.visibilityOfAllElements(noteTitleList));
    }

    public void waitForAddNoteButton(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.elementToBeClickable(addNoteButton));
    }

    public void waitForModal(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.visibilityOf(noteSaveChangesButton));
    }

    public List<String> getNoteTitleList() {
        return noteTitleList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }
}