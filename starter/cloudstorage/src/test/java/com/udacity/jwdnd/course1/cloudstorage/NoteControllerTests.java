package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.page.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.datasource.url=jdbc:h2:mem:NoteControllerTests"})
@Transactional
class NoteControllerTests {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    private static final User user1 = new User(null, "user1", null, "pass1", "first1", "last1");
    private static final User user2 = new User(null, "user2", null, "pass2", "first2", "last2");
    private static final Note note1a = new Note(null, "title1a", "description1a", null); // to remain same
    private static final Note note1b = new Note(null, "title1b", "description1b", null); // to have description edited
    private static final Note note1c = new Note(null, "title1c", "description1c", null); // to be deleted
    private static final Note note1d = new Note(null, "title1d", "description1d", null); // to have title edited
    private static final Note note2 = new Note(null, "title2", "description2", null);


    @BeforeAll
    static void beforeAll(@Autowired UserService userService,
                          @Autowired FileService fileService,
                          @Autowired NoteService noteService,
                          @Autowired CredentialService credentialService) {

        List<CrudService> crudServices = genCrudServices(fileService, noteService, credentialService);

        WebDriverManager.chromedriver().setup();

        userService.createAndUpdateObject(user1);
        addItemsToUser(user1, crudServices, note1a, note1b, note1c, note1d);

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
        homePageNoteTab.waitForItems(driver);
    }

    @Test
    void user1CanReadTheirNotesButNotUser2Notes() {
        loginAndGoToNotesTab();

        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);
        List<String> noteTitles = homePageNoteTab.getItemNameList();
        List<String> noteDescriptions = homePageNoteTab.getNoteDescriptionList();

        List<String> expectedNoteTitles = Arrays.asList(note1a.getNotetitle(), note1b.getNotetitle());
        List<String> expectedNoteDescriptions = Arrays.asList(note1a.getNotedescription(), note1d.getNotedescription());

        assertTrue(noteTitles.containsAll(expectedNoteTitles));
        assertTrue(noteDescriptions.containsAll(expectedNoteDescriptions));

        //check user1 cannot read user2's notes
        assertFalse(noteTitles.contains(note2.getNotetitle()));
        assertFalse(noteDescriptions.contains(note2.getNotedescription()));
    }

    @Test
    void userCanAddNewNote() {
        loginAndGoToNotesTab();
        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);
        homePageNoteTab.waitForAddButton(driver);
        click(driver, homePageNoteTab.addNoteButton);
        homePageNoteTab.waitForModal(driver);

        //add note
        String newTitle = "new title";
        String newDescription = "new description";
        homePageNoteTab.noteTitleInput.sendKeys(newTitle);
        homePageNoteTab.noteDescriptionInput.sendKeys(newDescription);
        click(driver, homePageNoteTab.noteSaveChangesButton);
        homePageNoteTab.waitForItems(driver);

        //check note is added
        List<String> noteTitles = homePageNoteTab.getItemNameList();
        assertTrue(noteTitles.contains(newTitle));
    }

    @Test
    void userCanDeleteTheirNote() {
        loginAndGoToNotesTab();
        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);

        List<String> noteTitles = homePageNoteTab.getItemNameList();
        assertTrue(noteTitles.contains(note1c.getNotetitle()));

        homePageNoteTab.deleteNoteByTitle(driver, note1c.getNotetitle());
        homePageNoteTab.waitForItems(driver);

        noteTitles = homePageNoteTab.getItemNameList();
        assertFalse(noteTitles.contains(note1c.getNotetitle()));
    }

    @Test
    void userCanEditTheirNoteDescription() {
        loginAndGoToNotesTab();
        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);

        List<String> noteTitles = homePageNoteTab.getItemNameList();
        assertTrue(noteTitles.contains(note1b.getNotetitle()));

        homePageNoteTab.editNoteByTitle(driver, note1b.getNotetitle());
        homePageNoteTab.waitForModal(driver);

        //edit note
        String newDescription = "edit description";
        homePageNoteTab.noteDescriptionInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), newDescription); // selecting all text to overwrite
        click(driver, homePageNoteTab.noteSaveChangesButton);
        homePageNoteTab.waitForItems(driver);

        String description = homePageNoteTab.getNoteDescriptionByTitle(driver, note1b.getNotetitle());
        assertEquals(newDescription, description);
    }

    @Test
    void userCanEditTheirNoteTitle() {
        loginAndGoToNotesTab();
        HomePageNoteTab homePageNoteTab = new HomePageNoteTab(driver);

        List<String> noteTitles = homePageNoteTab.getItemNameList();
        assertTrue(noteTitles.contains(note1d.getNotetitle()));
        String noteIdStr = homePageNoteTab.getIdStrOfNoteTitle(driver, note1d.getNotetitle()); //keep id to refer in future

        homePageNoteTab.editNoteByTitle(driver, note1d.getNotetitle());
        homePageNoteTab.waitForModal(driver);

        //edit note
        String newTitle = "edit title";
        homePageNoteTab.noteTitleInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), newTitle); // selecting all text to overwrite
        click(driver, homePageNoteTab.noteSaveChangesButton);
        homePageNoteTab.waitForItems(driver);

        String title = homePageNoteTab.getNoteTitleByIdStr(driver, noteIdStr);
        assertEquals(newTitle, title);
    }


}
