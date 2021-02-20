package com.udacity.jwdnd.course1.cloudstorage.page;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    private Utils() {
    }

    public static Integer WebDriverWaitTimeoutSeconds = 5;

    public static void login(WebDriver driver, int port, String username, String password) {
        driver.get("http://localhost:" + port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoginPage(driver);
        loginPage.login(driver, username, password);
    }

    //workaround to chrome webdriver click not working, using javascript executor instead
    public static void click(WebDriver driver, WebElement element) {
        JavascriptExecutor jexec = (JavascriptExecutor) driver;
        jexec.executeScript("arguments[0].click();", element);
    }

    public static void sendKeys(WebDriver driver, WebElement element, String keys) {
        JavascriptExecutor jexec = (JavascriptExecutor) driver;
        jexec.executeScript("arguments[0].value='" + keys + "';", element);
    }

    //These functions rely on the consist format of the html page
    public static List<String> getIdsList(WebDriver driver, String tableId) {
        String xpath = "//*[@id=\"" + tableId + "\"]/tbody/tr";
        List<WebElement> idsList = driver.findElements(By.xpath(xpath));
        return idsList.stream().map(item -> item.getAttribute("id")).collect(Collectors.toList());
    }

    public static String getIdOfTableProperty(WebDriver driver, String tableProperty, String tablePropertyName, String tableId) {
        List<String> fileIdsList = getIdsList(driver, tableId);
        return fileIdsList.stream().filter(
                id -> getTablePropertyById(driver, id, tableProperty).equals(tablePropertyName)
        ).collect(Collectors.toList()).get(0); // this only gets the id of the first matching itemname
    }

    public static Integer getIdNumberOfTableProperty(WebDriver driver, String tableProperty, String tablePropertyName, String tableId, String idPrefix) {
        String IdStr = getIdOfTableProperty(driver, tableProperty, tablePropertyName, tableId);
        return Integer.parseInt(IdStr.split(idPrefix)[1]);
    }

    public static String getTablePropertyById(WebDriver driver, String id, String tableProperty) {
        String xpath = "//*[@id=\"" + id + "\"]/" + tableProperty;
        WebElement title = driver.findElement(By.xpath(xpath));
        return title.getText();
    }

    public static void deleteItemByTableProperty(WebDriver driver, String tableProperty, String tablePropertyName, String tableId) {
        String idStr = getIdOfTableProperty(driver, tableProperty, tablePropertyName, tableId);
        String xpath = "//*[@id=\"delete-" + idStr + "\"]";
        WebElement button = driver.findElement(By.xpath(xpath));
        click(driver, button);
    }

    public static void editItemByTableProperty(WebDriver driver, String tableProperty, String tablePropertyName, String tableId) {
        String idStr = getIdOfTableProperty(driver, tableProperty, tablePropertyName, tableId);
        String xpath = "//*[@id=\"edit-" + idStr + "\"]";
        WebElement button = driver.findElement(By.xpath(xpath));
        click(driver, button);
    }

    public static String getTablePropertyOfItemByItemTableProperty(WebDriver driver, String requiredTableProperty, String givenTableProperty, String givenTablePropertyName, String tableId) {
        String idStr = getIdOfTableProperty(driver, givenTableProperty, givenTablePropertyName, tableId);
        String xpath = "//*[@id=\"" + idStr + "\"]/" + requiredTableProperty;
        WebElement requiredTablePropertyElement = driver.findElement(By.xpath(xpath));
        return requiredTablePropertyElement.getText();
    }
    // ^ consistent html format
}