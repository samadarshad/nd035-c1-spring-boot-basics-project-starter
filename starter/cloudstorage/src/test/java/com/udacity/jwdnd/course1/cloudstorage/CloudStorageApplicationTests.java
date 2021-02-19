package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.page.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.WebDriverWaitTimeoutSeconds;
import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.click;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.addItemsToUser;
import static com.udacity.jwdnd.course1.cloudstorage.utility.Utils.genCrudServices;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.datasource.url=jdbc:h2:mem:CloudStorageApplicationTests"})
@Transactional
class CloudStorageApplicationTests {

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
		driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (null != driver) {
			driver.quit();
		}
	}

//	public void signup(String username, String password) {
//		String firstname = "first";
//		String lastname = "last";
//
//		driver.get("http://localhost:" + port + "/signup");
//		SignupPage signupPage = new SignupPage(driver);
//		signupPage.signup(firstname, lastname, username, password);
//	}

//	public void login(String username, String password) {
//		driver.get("http://localhost:" + port + "/login");
//		LoginPage loginPage = new LoginPage(driver);
//
//		loginPage.login(driver, username, password);
//	}

//	public void signupAndLoginAndRedirectToHomePage(String username) {
//		String password = "pass";
//		signup(username, password);
//		login(username, password);
//	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

//	@Test
//	public void whenUserSignsUpTheyCanLogin() throws InterruptedException {
//		signupAndLoginAndRedirectToHomePage("user");
//
//		HomePage homePage = new HomePage(driver);
//		assertTrue(homePage.isLoggedIn());
//	}

//	@Test
//	public void whenAddUserToDatabaseTheirUsernameIsNotAvailable() {
//		assertTrue(userService.isUsernameAvailable("user"));
//		User user = new User(null, "user", null, "pass", "first", "last");
//		userService.createAndUpdateObject(user);
//		assertFalse(userService.isUsernameAvailable("user"));
//	}

//	@Test
//	public void whenAddUserToDatabaseTheyCanLogin() {
//		User user = new User(null, "user", null, "pass", "first", "last");
//		userService.createAndUpdateObject(user);
//		login("user", "pass");
//
//		HomePage homePage = new HomePage(driver);
//		assertTrue(homePage.isLoggedIn());
//	}

//	@Test
//	public void newUserItemsAreEmpty() {
//		User user = new User(null, "user", null, "pass", "first", "last");
//		userService.createAndUpdateObject(user);
//		login("user", "pass");
//
//		HomePage homePage = new HomePage(driver);
//		assertTrue(homePage.isLoggedIn());
//	}

//	@Test
//	void incorrectLoginShowsError() {
//		login("user1", "badpassword");
//
//		LoginPage loginPage = new LoginPage(driver);
//		// assert error is showing
//	}
//
//	@Test
//	void successfulLogoutShowsLogoutMessage() {
//		login("user1", "pass1");
//	}

	@Test
	void user1CanLogin() throws InterruptedException {
		Utils.login(driver, port, "user1", "pass1");
		HomePage homePage = new HomePage(driver);
		homePage.waitForLogin(driver);
		Thread.sleep(1000);
	}

	@Test
	void timeoutExeptionIfElementDoesntLoad() {
		WebDriverWait wait = new WebDriverWait(driver, WebDriverWaitTimeoutSeconds);
		assertThrows(
				TimeoutException.class,
				() -> wait.until(ExpectedConditions.elementToBeClickable(By.id("nonexisting-element")))
		);
	}


}
