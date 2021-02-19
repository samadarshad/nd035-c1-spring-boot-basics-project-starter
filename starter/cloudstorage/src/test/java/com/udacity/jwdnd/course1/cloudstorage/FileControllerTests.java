package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.page.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.addItemsToUser;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.genCrudServices;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.datasource.url=jdbc:h2:mem:FileControllerTests"})
@Transactional
class FileControllerTests {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    private static final User user1 = new User(null, "user1", null, "pass1", "first1", "last1");
    private static final User user2 = new User(null, "user2", null, "pass2", "first2", "last2");
    private static final byte [] fileData1a = "Hello World1a".getBytes(StandardCharsets.UTF_8);
    private static final byte [] fileData1b = "Hello World1b".getBytes(StandardCharsets.UTF_8);
    private static final byte [] fileData1c = "Hello World1c".getBytes(StandardCharsets.UTF_8);
    private static final byte [] fileData2 = "Hello World2".getBytes(StandardCharsets.UTF_8);
    private static final File file1a = new File(null, "fileName1a", "application/octet-stream", (long) fileData1a.length, null, fileData1a); // to remain same
    private static final File file1b = new File(null, "fileName1b", "application/octet-stream", (long) fileData1b.length, null, fileData1b); // to remain same
    private static final File file1c = new File(null, "fileName1c", "application/octet-stream", (long) fileData1c.length, null, fileData1c); // to be deleted
    private static final File file2 = new File(null, "fileName2", "application/octet-stream", (long) fileData2.length, null, fileData2);

    private static final String downloadsDirectory = System.getProperty("user.dir") + java.io.File.separator + "testDownloads";
    private static final String uploadsDirectory = System.getProperty("user.dir") + java.io.File.separator + "testUploads";
    private static final long fileTransferWaitTime = 1000;

    @BeforeAll
    static void beforeAll(@Autowired UserService userService,
                          @Autowired FileService fileService,
                          @Autowired NoteService noteService,
                          @Autowired CredentialService credentialService) {

        List<CrudService> crudServices = genCrudServices(fileService, noteService, credentialService);

        WebDriverManager.chromedriver().setup();

        userService.createAndUpdateObject(user1);
        addItemsToUser(user1, crudServices, file1a, file1b, file1c);

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

    private void clearDownloadsDirectory() {
        java.io.File folder = new java.io.File(downloadsDirectory);
        for(java.io.File file: folder.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    private void loginAndGoToFilesTab() {
        Utils.login(driver, port, "user1", "pass1");
        HomePageFileTab homePageFileTab = new HomePageFileTab(driver);
        homePageFileTab.waitForNav(driver);
        Utils.click(driver, homePageFileTab.navFilesTab);
        homePageFileTab.waitForFiles(driver);
    }

    private void testDownloadOf(File file) throws InterruptedException, IOException {
        //download file
        String fileName = file.getFileName();
        HomePageFileTab homePageFileTab = new HomePageFileTab(driver);
        homePageFileTab.downloadFileByFilename(driver, fileName);
        Thread.sleep(fileTransferWaitTime); // wait for download

        //check file
        byte[] fileContent = Files.readAllBytes(Path.of(downloadsDirectory + java.io.File.separator + fileName));
        assertArrayEquals(fileContent, file.getFileData());
    }

    @Test
    void user1CanReadFilenames() {
        loginAndGoToFilesTab();
        HomePageFileTab homePageFileTab = new HomePageFileTab(driver);
        List<String> fileNames = homePageFileTab.getFileNameList();

        List<String> expectedFileNames = Arrays.asList(file1a.getFileName(), file1b.getFileName());
        assertTrue(fileNames.containsAll(expectedFileNames));
    }

    @Test
    void user1CanDownloadTheirFiles() throws InterruptedException, IOException {
        loginAndGoToFilesTab();
        HomePageFileTab homePageFileTab = new HomePageFileTab(driver);
        List<String> fileNames = homePageFileTab.getFileNameList();

        //test file1a
        testDownloadOf(file1a);

        //test file1b
        testDownloadOf(file1b);
    }

    @Test
    void user1CanDownloadTheirFilesViaUrl() throws InterruptedException, IOException {
        Utils.login(driver, port, "user1", "pass1");

        //test file1a
        driver.get("http://localhost:" + port + "/files/" + file1a.getFileId());
        Thread.sleep(fileTransferWaitTime); // wait for download
        byte[] fileContent = Files.readAllBytes(Path.of(downloadsDirectory + java.io.File.separator + file1a.getFileName()));
        assertArrayEquals(fileContent, file1a.getFileData());
    }

    @Test
    void userCanUploadFileAndDownloadIt() throws InterruptedException, IOException {
        String fileName = "fileUpload1a";
        String filePath = uploadsDirectory + java.io.File.separator + fileName;

        loginAndGoToFilesTab();
        HomePageFileTab homePageFileTab = new HomePageFileTab(driver);

        //upload
        homePageFileTab.uploadFile(driver, filePath);
        Thread.sleep(fileTransferWaitTime);

        //check if filename exists
        homePageFileTab.waitForFiles(driver);
        List<String> fileNames = homePageFileTab.getFileNameList();
        assertTrue(fileNames.contains(fileName));

        //download
        homePageFileTab.downloadFileByFilename(driver, fileName);
        Thread.sleep(fileTransferWaitTime);

        //check contents
        byte[] downloadedFileContent = Files.readAllBytes(Path.of(downloadsDirectory + java.io.File.separator + fileName));
        byte[] originalUploadedFileContent = Files.readAllBytes(Path.of(filePath));
        assertArrayEquals(originalUploadedFileContent, downloadedFileContent);
    }

    @Test
    void userCanDeleteFile() {
        loginAndGoToFilesTab();
        HomePageFileTab homePageFileTab = new HomePageFileTab(driver);
        List<String> fileNames = homePageFileTab.getFileNameList();

        //select file1c
        String fileName = file1c.getFileName();
        int fileId = homePageFileTab.getIdOfFilename(driver, fileName);

        //delete file
        assertTrue(fileNames.contains(fileName));
        homePageFileTab.deleteFileByFilename(driver, fileName);

        //check filename doesnt exist
        fileNames = homePageFileTab.getFileNameList();
        assertFalse(fileNames.contains(fileName));

        //attempt to download deleted file
        driver.get("http://localhost:" + port + "/files/" + fileId);
        java.io.File downloadedFile = new java.io.File(downloadsDirectory + java.io.File.separator + fileName);
        assertFalse(downloadedFile.exists());
    }

    @Test
    void unauthenticatedUserCannotDownloadUser1File() throws InterruptedException {
        driver.get("http://localhost:" + port + "/files/" + file2.getFileId());
        Thread.sleep(fileTransferWaitTime); // wait for download
        java.io.File downloadedFile = new java.io.File(downloadsDirectory + java.io.File.separator + file2.getFileName());

        assertFalse(downloadedFile.exists());
    }

    @Test
    void user1CannotDownloadUser2File() throws InterruptedException {
        Utils.login(driver, port, "user1", "pass1");
        driver.get("http://localhost:" + port + "/files/" + file2.getFileId());
        Thread.sleep(fileTransferWaitTime); // wait for download
        java.io.File downloadedFile = new java.io.File(downloadsDirectory + java.io.File.separator + file2.getFileName());

        assertFalse(downloadedFile.exists());
    }
}
