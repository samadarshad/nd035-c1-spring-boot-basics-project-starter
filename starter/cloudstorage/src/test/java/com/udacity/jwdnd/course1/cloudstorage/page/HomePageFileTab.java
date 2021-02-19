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

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.*;

public class HomePageFileTab {
    @FindBy(xpath = "//*[@id=\"fileTable\"]/tbody/tr/th")
    private List<WebElement> fileNameList;

    @FindBy(xpath = "//*[@id=\"fileTable\"]/tbody/tr/td/a")
    private List<WebElement> fileDownloadLinkList;

    @FindBy(id = "nav-files-tab")
    public WebElement navFilesTab;

    @FindBy(id = "fileUpload")
    public WebElement fileUpload;

    @FindBy(id = "upload-button")
    public WebElement uploadButton;

    public String tableName = "fileTable";
    public String idPrefix = "file";

    public HomePageFileTab (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public List<String> getFileNameList() {
        return fileNameList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void waitForNav(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.visibilityOf(navFilesTab));
    }

    public void waitForFiles(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.visibilityOfAllElements(fileNameList));
    }

    public Integer getIdOfFilename(WebDriver driver, String filename) {
        return Utils.getIdOfItemName(driver, filename, tableName, idPrefix);
    }

    public void deleteFileByFilename(WebDriver driver, String filename) {
        Utils.deleteItemByItemName(driver, filename, tableName);
    }

    public void downloadFileByFilename(WebDriver driver, String filename) {
        Utils.editItemByItemName(driver, filename, tableName);
    }

    public void uploadFile(WebDriver driver, String filePath) {
        fileUpload.sendKeys(filePath);
        click(driver, uploadButton);
    }
}