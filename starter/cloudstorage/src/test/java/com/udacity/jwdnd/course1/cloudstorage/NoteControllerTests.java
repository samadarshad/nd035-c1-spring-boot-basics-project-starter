package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.page.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.click;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.addItemsToUser;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.genCrudServices;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class NoteControllerTests {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;

    private static final User user1 = new User(null, "user1", null, "pass1", "first1", "last1");
    private static final User user2 = new User(null, "user2", null, "pass2", "first2", "last2");
    private static final Note note1 = new Note(null, "title1", "description1", null);
    private static final Note note2 = new Note(null, "title2", "description2", null);


    @BeforeAll
    static void beforeAll(@Autowired UserService userService,
                          @Autowired FileService fileService,
                          @Autowired NoteService noteService,
                          @Autowired CredentialService credentialService) {

        List<CrudService> crudServices = genCrudServices(fileService, noteService, credentialService);

        WebDriverManager.chromedriver().setup();

        userService.createAndUpdateObject(user1);
        addItemsToUser(user1, crudServices, note1);

        userService.createAndUpdateObject(user2);
        addItemsToUser(user2, crudServices, note2);
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

    @Test
    void user1CanReadTheirNotes() throws InterruptedException {
        Utils.login(driver, port, "user1", "pass1");
        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);
        homePageNoteTab.waitForNotesTab(driver);
        click(driver, homePageNoteTab.navNotesTab);
        homePageNoteTab.waitForNotes(driver);
        List<String> noteTitles = homePageNoteTab.getNoteTitleList();

        List<String> expectedNoteTitles = Arrays.asList("title1");
        assertEquals(noteTitles, expectedNoteTitles);
    }

}
