package com.udacity.jwdnd.course1.cloudstorage.page;


import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserItems;
import com.udacity.jwdnd.course1.cloudstorage.services.CrudService;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    private Utils() {
    }

    public static Integer WebDriverWaitTimeoutSeconds = 5;

    public static void login(WebDriver driver, int port, String username, String password) {
        driver.get("http://localhost:" + port + "/login");
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(driver, username, password);
    }

    public static void click(WebDriver driver, WebElement element) {
        JavascriptExecutor jexec = (JavascriptExecutor) driver;
        jexec.executeScript("arguments[0].click();", element);
    }

    public static void sendKeys(WebDriver driver, WebElement element, String keys) {
        JavascriptExecutor jexec = (JavascriptExecutor) driver;
        jexec.executeScript("arguments[0].value='" + keys + "';", element);
    }

}