package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.page.Utils;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.WebDriverWaitTimeoutSeconds;
import static com.udacity.jwdnd.course1.cloudstorage.page.Utils.click;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.datasource.url=jdbc:h2:mem:SignupControllerTests"})
@Transactional
class SignupControllerTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;

	private static final User user1 = new User(null, "user1", null, "pass1", "first1", "last1");
	private static final User user2 = new User(null, "user2", null, "pass2", "first2", "last2");

	@BeforeAll
	static void beforeAll(@Autowired UserService userService) {
		WebDriverManager.chromedriver().setup();

		userService.createAndUpdateObject(user1);
		userService.createAndUpdateObject(user2);
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

	public void signup(String username, String password) {
		String firstname = "first";
		String lastname = "last";

		driver.get("http://localhost:" + port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.waitForPage(driver);
		signupPage.signup(firstname, lastname, username, password);
		signupPage.waitForPage(driver);
	}

	public void signupAndLoginAndRedirectToHomePage(String username, String password) {
		signup(username, password);
		Utils.login(driver, port, username, password);
	}

	private void login() {
		Utils.login(driver, port, "user1", "pass1");
		HomePage homePage = new HomePage(driver);
		homePage.waitForLogin(driver);
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void newUserCanSignupAndLogin() throws InterruptedException {
		signupAndLoginAndRedirectToHomePage("user", "pass");

		HomePage homePage = new HomePage(driver);
		homePage.waitForLogin(driver);
		assertNotNull(homePage.logoutButton);
	}

	@Test
	public void errorIfSignupWithExistingUsername() {
		signup("user1", "pass");
		SignupPage signupPage = new SignupPage(driver);
		assertNotNull(signupPage.errorMsg);
	}


	@Test
	void incorrectLoginShowsError() {
		Utils.login(driver, port, "user1", "badpassword");

		LoginPage loginPage = new LoginPage(driver);
		assertNotNull(loginPage.errorMsg);
	}

	@Test
	void logoutShowsLogoutMsg() {
		login();

		HomePage homePage = new HomePage(driver);
		click(driver, homePage.logoutButton);

		LoginPage loginPage = new LoginPage(driver);
		loginPage.waitForLoginPage(driver);
		assertNotNull(loginPage.logoutMsg);
	}


	@Test
	void user1CanLogin() {
		login();
		HomePage homePage = new HomePage(driver);
		assertNotNull(homePage.logoutButton);
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
