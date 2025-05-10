package tests;

import org.example.pages.LoginPage;
import org.example.pages.MainPage;
import org.example.pages.PointsPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PointsTest extends BaseTest {

    @Test
    public void viewPointsPageTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toPointsPage();
            PointsPage pointsPage = new PointsPage(driver, wait);
            assertTrue(pointsPage.isPointsPage());
        });
    }

    @Test
    public void viewExtendedListOfSubjectPointsTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toPointsPage();
            PointsPage pointsPage = new PointsPage(driver, wait);
            pointsPage.extendSubject();
            assertTrue(pointsPage.isExtendedSubject());
        });
    }

    @Test
    public void switchSemesterTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toPointsPage();
            PointsPage pointsPage = new PointsPage(driver, wait);
            pointsPage.switchSemester(1);
            assertTrue(pointsPage.isSwitchSemester());
            pointsPage.switchSemester(7);
            assertTrue(driver.getCurrentUrl().contains("semester=7"));
        });
    }
}
