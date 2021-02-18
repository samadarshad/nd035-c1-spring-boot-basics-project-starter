package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class HomePageFileTab {
    @FindBy(xpath = "//*[@id=\"fileTable\"]/tbody/tr/th")
    private List<WebElement> fileNameList;

    @FindBy(xpath = "//*[@id=\"fileTable\"]/tbody/tr/td/a")
    private List<WebElement> fileDownloadLinkList;

    @FindBy(id = "nav-files-tab")
    public WebElement navFilesTab;

    public HomePageFileTab (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public List<String> getFileNameList() {
        return fileNameList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void waitForFiles(WebDriver driver) throws InterruptedException {
        //wait-until isnt working as intended, so just using a delay
        Thread.sleep(500);
//        new WebDriverWait(driver, 3000)
//                .until(ExpectedConditions.visibilityOfAllElements(fileNameList));
//                .until(ExpectedConditions.elementToBeClickable(fileDownloadLinkList.get(0)));
    }

    public void downloadFile(int index) {
        fileDownloadLinkList.get(index).click();
    }
}