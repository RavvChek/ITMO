package tests;

import org.example.pages.BasePage;
import org.example.pages.LoginPage;
import org.example.pages.MainPage;
import org.example.pages.RequestsPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestsTest extends BaseTest {

    @Test
    public void viewRequestsPageTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toRequestsPage();
            RequestsPage requestsPage = new RequestsPage(driver, wait);
            assertTrue(requestsPage.isRequestsPage());
        });
    }

    @Test
    public void makeRequestTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toRequestsPage();
            RequestsPage requestsPage = new RequestsPage(driver, wait);
            requestsPage.makeRequest();
            assertTrue(requestsPage.isInvalidFeedback());
        });
    }
}
