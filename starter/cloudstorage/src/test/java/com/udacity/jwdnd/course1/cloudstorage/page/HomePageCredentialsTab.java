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
    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/th")
    private List<WebElement> credentialsUrlsList;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[2]")
    private List<WebElement> credentialsUsernamesList;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[3]")
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
        return Utils.getIdOfTableProperty(driver, "th", noteTitle, tableName);
//        return Utils.getIdStrOfItemName(driver, noteTitle, tableName);

    }

    public String getCredentialUrlByIdStr(WebDriver driver, String id) {
        return Utils.getTablePropertyById(driver, id, "th");
//        return Utils.getItemNameByIdStr(driver, id);
    }

    public List<String> getCredentialUrlList() {
        return credentialsUrlsList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void deleteCredentialByUrl(WebDriver driver, String url) {
        Utils.deleteItemByTableProperty(driver, "th", url, tableName);
//        Utils.deleteItemByItemName(driver, url, tableName);
    }

    public void editCredentialByUrl(WebDriver driver, String url) {
        Utils.editItemByTableProperty(driver, "th", url, tableName);
    }

    public String getCredentialUsernameByUrl(WebDriver driver, String url) {
        return Utils.getTablePropertyOfItemByItemTableProperty(driver, "td[2]", "th", url, tableName);
//        return Utils.getColumnOfItemByItemName(driver, url, 2, tableName);
//        return Utils.getColumnOfItemByItemName(driver, url, 2, tableName);
    }

    public String getCredentialPasswordByUrl(WebDriver driver, String url) {
        return Utils.getTablePropertyOfItemByItemTableProperty(driver, "td[3]", "th", url, tableName);
//        return Utils.getColumnOfItemByItemName(driver, url, 3, tableName);
    }
}