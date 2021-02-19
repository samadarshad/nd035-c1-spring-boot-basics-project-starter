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
import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.click;

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

    public String getIdStrOfItemName(WebDriver driver, String noteTitle) {
        return Utils.getIdStrOfItemName(driver, noteTitle, "noteTable");
    }

    public Integer getIdOfItemName(WebDriver driver, String filename) {
        return Utils.getIdOfItemName(driver, filename, "noteTable", "note");
    }

    public String getItemNameByIdStr(WebDriver driver, String id) {
        return Utils.getItemNameByIdStr(driver, id);
    }

    public List<String> getItemNameList() {
        return noteTitleList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public List<String> getNoteDescriptionList() {
        return noteDescriptionList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void deleteNoteByTitle(WebDriver driver, String title) {
        Utils.deleteItemByItemName(driver, title, "noteTable");
    }

    public void editNoteByTitle(WebDriver driver, String title) {
        Utils.editItemByItemName(driver, title, "noteTable");
    }

//    public void deleteItemByItemName(WebDriver driver, String itemName) {
//        String idStr = getIdStrOfItemName(driver, itemName);
//        String xpath = "//*[@id=\"" + idStr + "\"]/td[1]/form/button";
//        WebElement deleteButton = driver.findElement(By.xpath(xpath));
//        click(driver, deleteButton);
//    }
//
//    public void editItemByItemName(WebDriver driver, String title) {
//        String idStr = getIdStrOfItemName(driver, title);
//        String xpath = "//*[@id=\"" + idStr + "\"]/td[1]/button";
//        WebElement editButton = driver.findElement(By.xpath(xpath));
//        click(driver, editButton);
//    }

    public String getNoteDescriptionByTitle(WebDriver driver, String title) {
        String noteId = getIdStrOfItemName(driver, title);
        String xpath = "//*[@id=\"" + noteId + "\"]/td[2]";
        WebElement description = driver.findElement(By.xpath(xpath));
        return description.getText();
    }
}