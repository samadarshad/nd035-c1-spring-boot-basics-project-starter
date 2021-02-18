package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.addItemsToUser;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.genCrudServices;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class FileControllerTests {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private CredentialService credentialService;

    private static final User user1 = new User(null, "user1", null, "pass1", "first1", "last1");
    private static final User user2 = new User(null, "user2", null, "pass2", "first2", "last2");
    private static final byte [] fileData1 = "Hello World1".getBytes(StandardCharsets.UTF_8);
    private static final byte [] fileData2 = "Hello World2".getBytes(StandardCharsets.UTF_8);
    private static final Note note1 = new Note(null, "title1", "description1", null);
    private static final Note note2 = new Note(null, "title2", "description2", null);
    private static final Credential credential1 = new Credential(null, "url1", "username1", null, "password1", null);
    private static final Credential credential2 = new Credential(null, "url2", "username2", null, "password2", null);
    private static final File file1 = new File(null, "fileName1", "application/octet-stream", (long) fileData1.length, null, fileData1);
    private static final File file2 = new File(null, "fileName2", "application/octet-stream", (long) fileData2.length, null, fileData2);

    private static final String downloadsDirectory = System.getProperty("user.dir") + java.io.File.separator + "testDownloads";


    @BeforeAll
    static void beforeAll(@Autowired UserService userService,
                          @Autowired FileService fileService,
                          @Autowired NoteService noteService,
                          @Autowired CredentialService credentialService) {

        List<CrudService> crudServices = genCrudServices(fileService, noteService, credentialService);

        WebDriverManager.chromedriver().setup();

        userService.createAndUpdateObject(user1);
        addItemsToUser(user1, crudServices, note1, credential1, file1);

        userService.createAndUpdateObject(user2);
        addItemsToUser(user2, crudServices, note2, credential2, file2);
    }

    @BeforeEach
    public void beforeEach() {
        //set temp download directory for file tests
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap();
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", downloadsDirectory);
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void afterEach() {
        if (null != driver) {
            driver.quit();
        }
        clearDownloadsDirectory();
    }

    public void clearDownloadsDirectory() {
        java.io.File folder = new java.io.File(downloadsDirectory);
        for(java.io.File file: folder.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    public void login(String username, String password) {
        driver.get("http://localhost:" + port + "/login");
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);
    }

    @Test
    void user1CanReadTheirExistingItems() throws InterruptedException {
        login("user1", "pass1");

        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isLoggedIn());

        homePage.navFilesTab.click();
        homePage.waitForFiles(driver);
        List<String> fileNames = homePage.getFileNameList();
        homePage.downloadFile(0);
        java.io.File downloadedFile = new java.io.File(downloadsDirectory + java.io.File.separator + fileNames.get(0));
        Thread.sleep(1000); // wait for download
        assertTrue(downloadedFile.exists());
    }

    @Test
    void user1CanEditTheirExistingItems() {

    }

    @Test
    void user1CanDeleteTheirExistingItems() {

    }

    @Test
    void user1CannotReadUser2Items() {

    }

    @Test
    void user1CannotEditUser2Items() {

    }

    @Test
    void user1CannotDeleteUser2Items() {

    }




}
