package com.udacity.jwdnd.course1.cloudstorage.page;


import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserItems;
import com.udacity.jwdnd.course1.cloudstorage.services.CrudService;
import org.openqa.selenium.WebDriver;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    private Utils() {
    }

    public static void login(WebDriver driver, int port, String username, String password) {
        driver.get("http://localhost:" + port + "/login");
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);
    }

}