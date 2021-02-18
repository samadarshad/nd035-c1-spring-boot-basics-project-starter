package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
	private static final File file1 = new File(null, "fileName1", "contentType1", (long) fileData1.length, null, fileData1);
	private static final File file2 = new File(null, "fileName2", "contentType2", (long) fileData2.length, null, fileData2);


	private static List<CrudService> genCrudServices(CrudService... crudServices) {
		List<CrudService> list = new ArrayList<>();
		for (CrudService service : crudServices) {
			list.add(service);
		}
		return list;
	}

	public static CrudService itemToServiceMapper(UserItems item, List<CrudService> crudServices) {
		for (CrudService service : crudServices) {
			if (item.getClass() == service.getObjectType()) {
				return service;
			}
		}
		throw new IllegalStateException("Unexpected value: " + item.getClass());
	}

	public static void addItemsToUser(User user, List<CrudService> crudServices, UserItems... items) {
		for (UserItems item : items) {
			item.setUserId(user.getUserId());
			CrudService crudService = itemToServiceMapper(item, crudServices);
			crudService.createAndUpdateObject(item);
		}
	}

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
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	public void signup(String username, String password) {
		String firstname = "first";
		String lastname = "last";

		driver.get("http://localhost:" + port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstname, lastname, username, password);
	}

	public void login(String username, String password) {
		driver.get("http://localhost:" + port + "/login");
		LoginPage loginPage = new LoginPage(driver);

		loginPage.login(username, password);
	}

	public void signupAndLoginAndRedirectToHomePage(String username) {
		String password = "pass";
		signup(username, password);
		login(username, password);
	}



	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void whenUserSignsUpTheyCanLogin() throws InterruptedException {
		signupAndLoginAndRedirectToHomePage("user");

		HomePage homePage = new HomePage(driver);
		assertTrue(homePage.isLoggedIn());
	}

	@Test
	public void whenAddUserToDatabaseTheirUsernameIsNotAvailable() {
		assertTrue(userService.isUsernameAvailable("user"));
		User user = new User(null, "user", null, "pass", "first", "last");
		userService.createAndUpdateObject(user);
		assertFalse(userService.isUsernameAvailable("user"));
	}

	@Test
	public void whenAddUserToDatabaseTheyCanLogin() {
		User user = new User(null, "user", null, "pass", "first", "last");
		userService.createAndUpdateObject(user);
		login("user", "pass");

		HomePage homePage = new HomePage(driver);
		assertTrue(homePage.isLoggedIn());
	}



}
