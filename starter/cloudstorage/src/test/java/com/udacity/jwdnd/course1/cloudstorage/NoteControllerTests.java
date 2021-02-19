package com.udacity.jwdnd.course1.cloudstorage;

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

import java.util.Arrays;
import java.util.List;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.click;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.addItemsToUser;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.genCrudServices;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.datasource.url=jdbc:h2:mem:NoteControllerTests"})
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

    private void loginAndGoToNotesTab() {
        Utils.login(driver, port, "user1", "pass1");
        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);
        homePageNoteTab.waitForNav(driver);
        Utils.click(driver, homePageNoteTab.navNotesTab);
        homePageNoteTab.waitForNotes(driver);
    }

    @Test
    void user1CanReadTheirNotes() {
        loginAndGoToNotesTab();

        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);
        List<String> noteTitles = homePageNoteTab.getNoteTitleList();

        List<String> expectedNoteTitles = Arrays.asList("title1");
        assertEquals(noteTitles, expectedNoteTitles);
    }

    @Test
    void userCanAddNewNote() {
        loginAndGoToNotesTab();
        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);
        homePageNoteTab.waitForAddNoteButton(driver);
        click(driver, homePageNoteTab.addNoteButton);
        homePageNoteTab.waitForModal(driver);

        //add note
        String newTitle = "new title";
        String newDescription = "new description";
        homePageNoteTab.noteTitleInput.sendKeys(newTitle);
        homePageNoteTab.noteDescriptionInput.sendKeys(newDescription);
        click(driver, homePageNoteTab.noteSaveChangesButton);
        homePageNoteTab.waitForNotes(driver);

        //check note is added
        List<String> noteTitles = homePageNoteTab.getNoteTitleList();
        assertTrue(noteTitles.contains(newTitle));
    }


}
