package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private LoginPage loginPage;
	private SignupPage signupPage;

	@Autowired
	private UserService userService;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
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
		signupPage = new SignupPage(driver);
		signupPage.signup(firstname, lastname, username, password);
	}

	public void login(String username, String password) {
		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);

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

		Thread.sleep(3000);
	}

	@Test
	public void whenAddUserToDatabaseTheirUsernameIsNotAvailable() throws InterruptedException {
		assertTrue(userService.isUsernameAvailable("user"));
		User user = new User(null, "user", null, "pass", "first", "last");
		userService.createAndUpdateObject(user);
		assertFalse(userService.isUsernameAvailable("user"));
	}

	@Test
	public void whenAddUserToDatabaseTheyCanLogin() throws InterruptedException {
		User user = new User(null, "user", null, "pass", "first", "last");
		userService.createAndUpdateObject(user);
		login("user", "pass");

        Thread.sleep(3000);
	}

}
