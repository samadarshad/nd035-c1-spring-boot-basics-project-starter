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

    // storing passwords separately here because it will be encrypted when added to user
    private static final String password1a = "password1a";
    private static final String password1b = "password1b";
    private static final String password1c = "password1c";
    private static final String password1d = "password1d";
    private static final String password1e = "password1e";
    private static final Credential credential1a = new Credential(null, "url1a", "username1a", null, password1a, null); // to remain same
    private static final Credential credential1b = new Credential(null, "url1b", "username1b", null, password1b, null); // to have url edited
    private static final Credential credential1c = new Credential(null, "url1c", "username1c", null, password1c, null); // to have username edited
    private static final Credential credential1d = new Credential(null, "url1d", "username1d", null, password1d, null); // to have password edited
    private static final Credential credential1e = new Credential(null, "url1e", "username1e", null, password1e, null); // to be deleted
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
    void user1CanReadTheirCredentialsButNotUser2Credentials() {
        loginAndGoToCredentialsTab();

        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        List<String> urls = homePageCredentialsTab.getCredentialUrlList();
        List<String> usernames = homePageCredentialsTab.getCredentialUsernameList();

        List<String> expectedUrls = Arrays.asList(credential1a.getUrl(), credential1c.getUrl(), credential1d.getUrl());
        List<String> expectedUsernames = Arrays.asList(credential1a.getUsername(), credential1b.getUsername(), credential1d.getUsername());

        assertTrue(urls.containsAll(expectedUrls));
        assertTrue(usernames.containsAll(expectedUsernames));

        //check user1 cannot read user2's items
        assertFalse(urls.contains(credential2.getUrl()));
        assertFalse(usernames.contains(credential2.getUsername()));
    }

    @Test
    void passwordsListIsEncrypted() {
        loginAndGoToCredentialsTab();

        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        List<String> passwords = homePageCredentialsTab.getCredentialPasswordList();

        assertFalse(passwords.contains(password1a));
        assertFalse(passwords.contains(password1b));
        assertFalse(passwords.contains(password1c));
        assertFalse(passwords.contains(password1d));
        assertFalse(passwords.contains(password1e));
    }

    @Test
    void decryptPasswordWhenEditItem() {
        loginAndGoToCredentialsTab();

        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        homePageCredentialsTab.editCredentialByUrl(driver, credential1a.getUrl());
        homePageCredentialsTab.waitForModal(driver);

        assertEquals(homePageCredentialsTab.credentialPasswordInput.getAttribute("value"), password1a);
    }

    @Test
    void editUrl() {
        loginAndGoToCredentialsTab();

        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        String id = homePageCredentialsTab.getIdOfCredentialUrl(driver, credential1b.getUrl()); // keeping id for future reference
        homePageCredentialsTab.editCredentialByUrl(driver, credential1b.getUrl());
        homePageCredentialsTab.waitForModal(driver);

        //edit item
        String newValue = "edit url";
        homePageCredentialsTab.credentialUrlInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), newValue); // selecting all text to overwrite
        click(driver, homePageCredentialsTab.saveChangesButton);
        homePageCredentialsTab.waitForItems(driver);

        String url = homePageCredentialsTab.getCredentialUrlById(driver, id);
        assertEquals(url, newValue);
    }

    @Test
    void editUsername() {
        loginAndGoToCredentialsTab();

        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        homePageCredentialsTab.editCredentialByUrl(driver, credential1c.getUrl());
        homePageCredentialsTab.waitForModal(driver);

        //edit item
        String newValue = "edit username";
        homePageCredentialsTab.credentialUsernameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), newValue); // selecting all text to overwrite
        click(driver, homePageCredentialsTab.saveChangesButton);
        homePageCredentialsTab.waitForItems(driver);

        String username = homePageCredentialsTab.getCredentialUsernameByUrl(driver, credential1c.getUrl());
        assertEquals(username, newValue);
    }

    @Test
    void editPassword() {
        loginAndGoToCredentialsTab();

        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        homePageCredentialsTab.editCredentialByUrl(driver, credential1d.getUrl());
        homePageCredentialsTab.waitForModal(driver);

        //edit item
        String newValue = "edit password";
        homePageCredentialsTab.credentialPasswordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), newValue); // selecting all text to overwrite
        click(driver, homePageCredentialsTab.saveChangesButton);
        homePageCredentialsTab.waitForItems(driver);

        //can only view decrypted password when click edit
        homePageCredentialsTab.editCredentialByUrl(driver, credential1d.getUrl());
        homePageCredentialsTab.waitForModal(driver);

        assertEquals(homePageCredentialsTab.credentialPasswordInput.getAttribute("value"), newValue);
    }

    @Test
    void delete() {
        loginAndGoToCredentialsTab();

        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        List<String> urls = homePageCredentialsTab.getCredentialUrlList();
        assertTrue(urls.contains(credential1e.getUrl()));

        homePageCredentialsTab.deleteCredentialByUrl(driver, credential1e.getUrl());
        homePageCredentialsTab.waitForItems(driver);

        urls = homePageCredentialsTab.getCredentialUrlList();
        assertFalse(urls.contains(credential1e.getUrl()));
    }

    @Test
    void create() {
        loginAndGoToCredentialsTab();

        HomePageCredentialsTab homePageCredentialsTab = new HomePageCredentialsTab(driver);
        click(driver, homePageCredentialsTab.addCredentialButton);
        homePageCredentialsTab.waitForModal(driver);

        String newUrl = "new url";
        homePageCredentialsTab.credentialUrlInput.sendKeys(newUrl);

        String newUsername = "new username";
        homePageCredentialsTab.credentialUsernameInput.sendKeys(newUsername);

        String newPassword = "new password";
        homePageCredentialsTab.credentialPasswordInput.sendKeys(newPassword);

        click(driver, homePageCredentialsTab.saveChangesButton);

        homePageCredentialsTab.waitForItems(driver);
        List<String> urls = homePageCredentialsTab.getCredentialUrlList();
        List<String> usernames = homePageCredentialsTab.getCredentialUsernameList();
        List<String> passwords = homePageCredentialsTab.getCredentialPasswordList();


        assertTrue(urls.contains(newUrl));
        assertTrue(usernames.contains(newUsername));
        //assert that the password is encrypted
        assertFalse(passwords.contains(newPassword));
    }

}
