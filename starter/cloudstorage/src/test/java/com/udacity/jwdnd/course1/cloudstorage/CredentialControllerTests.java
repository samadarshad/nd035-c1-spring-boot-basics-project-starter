package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePageCredentialsTab;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePageNoteTab;
import com.udacity.jwdnd.course1.cloudstorage.page.Utils;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.click;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.addItemsToUser;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.genCrudServices;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.datasource.url=jdbc:h2:mem:CredentialControllerTests"})
@Transactional
class CredentialControllerTests {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    private static final User user1 = new User(null, "user1", null, "pass1", "first1", "last1");
    private static final User user2 = new User(null, "user2", null, "pass2", "first2", "last2");
    private static final Credential credential1a = new Credential(null, "url1a", "username1a", null, "password1a", null); // to remain same
    private static final Credential credential1b = new Credential(null, "url1b", "username1b", null, "password1b", null); // to have url edited
    private static final Credential credential1c = new Credential(null, "url1c", "username1c", null, "password1c", null); // to have username edited
    private static final Credential credential1d = new Credential(null, "url1d", "username1d", null, "password1d", null); // to have password edited
    private static final Credential credential1e = new Credential(null, "url1e", "username1e", null, "password1e", null); // to be deleted
    private static final Credential credential2 = new Credential(null, "url2", "username2", null, "password2", null);


    @BeforeAll
    static void beforeAll(@Autowired UserService userService,
                          @Autowired FileService fileService,
                          @Autowired NoteService noteService,
                          @Autowired CredentialService credentialService) {

        List<CrudService> crudServices = genCrudServices(fileService, noteService, credentialService);

        WebDriverManager.chromedriver().setup();

        userService.createAndUpdateObject(user1);
        addItemsToUser(user1, crudServices, credential1a, credential1b, credential1c, credential1d, credential1e);

        userService.createAndUpdateObject(user2);
        addItemsToUser(user2, crudServices, credential2);
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (null != driver) {
            driver.quit();
        }
    }

    private void loginAndGoToCredentialsTab() {
        Utils.login(driver, port, "user1", "pass1");
        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        homePageCredentialsTab.waitForNav(driver);
        Utils.click(driver, homePageCredentialsTab.navCredentialsTab);
        homePageCredentialsTab.waitForItems(driver);
    }

    @Test
    void user1CanReadTheirCredentialsButNotUser2Notes() {
        loginAndGoToCredentialsTab();


    }




}
