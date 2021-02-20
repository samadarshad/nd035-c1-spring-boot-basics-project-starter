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

    public static final String credentialUrlTableProperty = "th";
    public static final String credentialUsernameTableProperty = "td[2]";
    public static final String credentialPasswordTableProperty = "td[3]";
    public static final String tableName = "credentialTable";
    public static final String idPrefix = "credential";

    @FindBy(xpath = "//*[@id=\"" + tableName + "\"]/tbody/tr/" + credentialUrlTableProperty)
    private List<WebElement> credentialsUrlsList;

    @FindBy(xpath = "//*[@id=\"" + tableName + "\"]/tbody/tr/" + credentialUsernameTableProperty)
    private List<WebElement> credentialsUsernamesList;

    @FindBy(xpath = "//*[@id=\"" + tableName + "\"]/tbody/tr/" + credentialPasswordTableProperty)
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

    public String getIdOfCredentialUrl(WebDriver driver, String url) {
        return Utils.getIdOfTableProperty(driver, credentialUrlTableProperty, url, tableName);

    }

    public String getCredentialUrlById(WebDriver driver, String id) {
        return Utils.getTablePropertyById(driver, id, credentialUrlTableProperty);
    }

    public List<String> getCredentialUrlList() {
        return credentialsUrlsList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public List<String> getCredentialUsernameList() {
        return credentialsUsernamesList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public List<String> getCredentialPasswordList() {
        return credentialsPasswordsList.stream().map(item -> item.getText()).collect(Collectors.toList());
    }

    public void deleteCredentialByUrl(WebDriver driver, String url) {
        Utils.deleteItemByTableProperty(driver, credentialUrlTableProperty, url, tableName);
    }

    public void editCredentialByUrl(WebDriver driver, String url) {
        Utils.editItemByTableProperty(driver, credentialUrlTableProperty, url, tableName);
    }

    public String getCredentialUsernameByUrl(WebDriver driver, String url) {
        return Utils.getTablePropertyOfItemByItemTableProperty(driver, credentialUsernameTableProperty, credentialUrlTableProperty, url, tableName);
    }

    public String getCredentialPasswordByUrl(WebDriver driver, String url) {
        return Utils.getTablePropertyOfItemByItemTableProperty(driver, credentialPasswordTableProperty, credentialUrlTableProperty, url, tableName);
    }
}