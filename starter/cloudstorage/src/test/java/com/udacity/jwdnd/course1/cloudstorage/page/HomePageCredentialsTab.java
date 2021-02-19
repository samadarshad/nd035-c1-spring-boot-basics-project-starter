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

public class HomePageCredentialsTab {
    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-credentials']/div[1]/table/tbody/tr/th")
    private List<WebElement> credentialsUrlsList;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-credentials']/div[1]/table/tbody/tr/td[2]")
    private List<WebElement> credentialsUsernamesList;

    @FindBy(xpath = "/html/body/div/div[@id='contentDiv']/div/div[@id='nav-credentials']/div[1]/table/tbody/tr/td[3]")
    private List<WebElement> credentialsPasswordsList;

    @FindBy(id = "nav-credentials-tab")
    public WebElement navCredentialsTab;

    @FindBy(xpath = "//*[@id=\"nav-credentials\"]/button")
    public WebElement addCredentialButton;

    @FindBy(xpath = "//*[@id=\"credential-url\"]")
    public WebElement credentialUrlInput;

    @FindBy(xpath = "//*[@id=\"credential-username\"]")
    public WebElement credentialUsernameInput;

    @FindBy(xpath = "//*[@id=\"credential-password\"]")
    public WebElement credentialPasswordInput;

    @FindBy(id = "credential-save-changes")
    public WebElement saveChangesButton;

    @FindBy(id = "credential-cancel-changes")
    public WebElement cancelChangesButton;

    public String tableName = "credentialTable";
    public String idPrefix = "credential";

    public HomePageCredentialsTab (WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void waitForNav(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.elementToBeClickable(navCredentialsTab));
    }

    public void waitForItems(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.visibilityOfAllElements(credentialsUrlsList));
    }

    public void waitForAddButton(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.elementToBeClickable(addCredentialButton));
    }

    public void waitForModal(WebDriver driver) {
        new WebDriverWait(driver, WebDriverWaitTimeoutSeconds)
                .until(ExpectedConditions.visibilityOf(saveChangesButton));
    }

    public String getIdStrOfItemName(WebDriver driver, String noteTitle) {
        return Utils.getIdStrOfItemName(driver, noteTitle, tableName);
    }

    public Integer getIdOfItemName(WebDriver driver, String filename) {
        return Utils.getIdOfItemName(driver, filename, tableName, idPrefix);
    }

    public String getCredentialUrlByIdStr(WebDriver driver, String id) {
        return Utils.getItemNameByIdStr(driver, id);
    }

    public List<String> getCredentialUrlList() {
        return credentialsUrlsList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void deleteCredentialByUrl(WebDriver driver, String url) {
        Utils.deleteItemByItemName(driver, url, tableName);
    }

    public void editCredentialByUrl(WebDriver driver, String url) {
        Utils.editItemByItemName(driver, url, tableName);
    }

    public String getCredentialUsernameByUrl(WebDriver driver, String url) {
        String idStr = getIdStrOfItemName(driver, url);
        String xpath = "//*[@id=\"" + idStr + "\"]/td[2]";
        WebElement description = driver.findElement(By.xpath(xpath));
        return description.getText();
    }

    public String getCredentialPasswordByUrl(WebDriver driver, String url) {
        String idStr = getIdStrOfItemName(driver, url);
        String xpath = "//*[@id=\"" + idStr + "\"]/td[3]";
        WebElement description = driver.findElement(By.xpath(xpath));
        return description.getText();
    }
}