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

    public static String getIdStrOfItemName(WebDriver driver, String itemName, String tableId) {
        List<String> fileIdsList = getIdsList(driver, tableId);
        return fileIdsList.stream().filter(
                id -> getItemNameByIdStr(driver, id).equals(itemName)
        ).collect(Collectors.toList()).get(0);
    }

    public static Integer getIdOfItemName(WebDriver driver, String filename, String tableId, String idPrefix) {
        String IdStr = getIdStrOfItemName(driver, filename, tableId);
        return Integer.parseInt(IdStr.split(idPrefix)[1]);
    }

    public static String getItemNameByIdStr(WebDriver driver, String id) {
        String xpath = "//*[@id=\"" + id + "\"]/th";
        WebElement title = driver.findElement(By.xpath(xpath));
        return title.getText();
    }
    // ^ consistent html format
}