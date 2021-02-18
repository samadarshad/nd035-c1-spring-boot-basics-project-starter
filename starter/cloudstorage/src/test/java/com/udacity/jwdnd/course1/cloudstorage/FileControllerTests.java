package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.page.Utils;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private static final User user1 = new User(null, "user1", null, "pass1", "first1", "last1");
    private static final User user2 = new User(null, "user2", null, "pass2", "first2", "last2");
    private static final byte [] fileData1a = "Hello World1a".getBytes(StandardCharsets.UTF_8);
    private static final byte [] fileData1b = "Hello World1b".getBytes(StandardCharsets.UTF_8);
    private static final byte [] fileData2 = "Hello World2".getBytes(StandardCharsets.UTF_8);
    private static final File file1a = new File(null, "fileName1a", "application/octet-stream", (long) fileData1a.length, null, fileData1a);
    private static final File file1b = new File(null, "fileName1b", "application/octet-stream", (long) fileData1b.length, null, fileData1b);
    private static final File file2 = new File(null, "fileName2", "application/octet-stream", (long) fileData2.length, null, fileData2);

    private static final String downloadsDirectory = System.getProperty("user.dir") + java.io.File.separator + "testDownloads";
    private static final long downloadWaitTime = 3000;

    @BeforeAll
    static void beforeAll(@Autowired UserService userService,
                          @Autowired FileService fileService,
                          @Autowired NoteService noteService,
                          @Autowired CredentialService credentialService) {

        List<CrudService> crudServices = genCrudServices(fileService, noteService, credentialService);

        WebDriverManager.chromedriver().setup();

        userService.createAndUpdateObject(user1);
        addItemsToUser(user1, crudServices, file1a, file1b);

        userService.createAndUpdateObject(user2);
        addItemsToUser(user2, crudServices, file2);
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

    @Test
    void user1CanReadFilenames() {
        Utils.login(driver, port, "user1", "pass1");
        HomePage homePage = new HomePage(driver);
        homePage.navFilesTab.click();
        homePage.waitForFiles(driver);
        List<String> fileNames = homePage.getFileNameList();

        List<String> expectedFileNames = Arrays.asList("fileName1");
        assertEquals(fileNames, expectedFileNames);
    }

    @Test
    void user1CanDownloadTheirFiles() throws InterruptedException, IOException {
        Utils.login(driver, port, "user1", "pass1");
        HomePage homePage = new HomePage(driver);
        homePage.navFilesTab.click();
        homePage.waitForFiles(driver);
        List<String> fileNames = homePage.getFileNameList();
        int i = 0;

        homePage.downloadFile(i);
        Thread.sleep(downloadWaitTime); // wait for download

        java.io.File downloadedFile = new java.io.File(downloadsDirectory + java.io.File.separator + fileNames.get(i));
        assertTrue(downloadedFile.exists());

        byte[] fileContent = Files.readAllBytes(Path.of(downloadsDirectory + java.io.File.separator + fileNames.get(i)));
        assertArrayEquals(fileContent, file1a.getFileData());

    }

    @Test
    void user1CanDownloadTheirFilesViaUrl() throws InterruptedException {
        Utils.login(driver, port, "user1", "pass1");
        driver.get("http://localhost:" + port + "/files/" + file1a.getFileId());
        Thread.sleep(downloadWaitTime); // wait for download
        java.io.File downloadedFile1a = new java.io.File(downloadsDirectory + java.io.File.separator + file1a.getFileName());

        assertTrue(downloadedFile1a.exists());

        driver.get("http://localhost:" + port + "/files/" + file1b.getFileId());
        Thread.sleep(downloadWaitTime); // wait for download
        java.io.File downloadedFile1b = new java.io.File(downloadsDirectory + java.io.File.separator + file1b.getFileName());

        assertTrue(downloadedFile1b.exists());
    }

    @Test
    void user1CannotDownloadUser2File() throws InterruptedException {
        Utils.login(driver, port, "user1", "pass1");
        driver.get("http://localhost:" + port + "/files/" + file2.getFileId());
        Thread.sleep(downloadWaitTime); // wait for download
        java.io.File downloadedFile = new java.io.File(downloadsDirectory + java.io.File.separator + file2.getFileName());


        assertFalse(downloadedFile.exists());
    }

}
