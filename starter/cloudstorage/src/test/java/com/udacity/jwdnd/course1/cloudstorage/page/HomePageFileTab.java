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

    public void downloadFile(WebDriver driver, int index) {
        click(driver, fileDownloadLinkList.get(index));
    }

    public List<String> getFileIdsList(WebDriver driver) {
        //*[@id="fileTable"]/tbody
        String xpath = "//*[@id=\"fileTable\"]/tbody/tr";
        List<WebElement> fileIdsList = driver.findElements(By.xpath(xpath));
        return fileIdsList.stream().map(item -> item.getAttribute("id")).collect(Collectors.toList());
    }

    public String getFilenameById(WebDriver driver, String id) {
        //*[@id="file4"]/th
        String xpath = "//*[@id=\"" + id + "\"]/th";
        WebElement filename = driver.findElement(By.xpath(xpath));
        return filename.getText();
    }

    public void downloadFileById(WebDriver driver, String id) {
        //*[@id="file4"]/td/a
        String xpath = "//*[@id=\"" + id + "\"]/td/a";
        WebElement downloadLink = driver.findElement(By.xpath(xpath));
        click(driver, downloadLink);
    }

    public void uploadFile(WebDriver driver, String filePath) {
        fileUpload.sendKeys(filePath);
        click(driver, uploadButton);
    }
}