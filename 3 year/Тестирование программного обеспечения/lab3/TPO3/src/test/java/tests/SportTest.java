package tests;

import com.sun.tools.javac.Main;
import org.example.pages.BasePage;
import org.example.pages.LoginPage;
import org.example.pages.MainPage;
import org.example.pages.SportPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SportTest extends BaseTest {

    @Test
    public void viewPointsSportTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            SportPage sportPage = new SportPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toSportPoints();
            assertTrue(sportPage.isSportPage());
        });
    }

    @Test
    public void signOnSportSectionTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            SportPage sportPage = new SportPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toSectionSportPage();
            sportPage.signOnSectionSPort();
            assertTrue(sportPage.isWindowSection());
        });
    }

}
