package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.By;
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

    public static final String noteTitleTableProperty = "th";
    public static final String noteDescriptionTableProperty = "td[2]";
    public static final String tableName = "noteTable";
    public static final String idPrefix = "note";

    @FindBy(xpath = "//*[@id=\"" + tableName + "\"]/tbody/tr/" + noteTitleTableProperty)
    private List<WebElement> noteTitleList;

    @FindBy(xpath = "//*[@id=\"" + tableName + "\"]/tbody/tr/" + noteDescriptionTableProperty)
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

    public void waitForItems(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.visibilityOfAllElements(noteTitleList));
    }

    public void waitForAddButton(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.elementToBeClickable(addNoteButton));
    }

    public void waitForModal(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.visibilityOf(noteSaveChangesButton));
    }

    public String getIdStrOfNoteTitle(WebDriver driver, String noteTitle) {
        return Utils.getIdOfTableProperty(driver, noteTitleTableProperty, noteTitle, tableName);
    }

    public String getNoteTitleByIdStr(WebDriver driver, String id) {
        return Utils.getTablePropertyById(driver, id, noteTitleTableProperty);
    }

    public List<String> getItemNameList() {
        return noteTitleList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public List<String> getNoteDescriptionList() {
        return noteDescriptionList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void deleteNoteByTitle(WebDriver driver, String title) {
        Utils.deleteItemByTableProperty(driver, noteTitleTableProperty, title, tableName);
    }

    public void editNoteByTitle(WebDriver driver, String title) {
        Utils.editItemByTableProperty(driver, noteTitleTableProperty, title, tableName);
    }

    public String getNoteDescriptionByTitle(WebDriver driver, String title) {
        return Utils.getTablePropertyOfItemByItemTableProperty(driver, noteDescriptionTableProperty, noteTitleTableProperty, title, tableName);
    }
}