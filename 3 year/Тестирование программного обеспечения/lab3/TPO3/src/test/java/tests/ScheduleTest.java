package tests;

import org.example.pages.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScheduleTest extends BaseTest {



    @Test
    public void viewSchedulePage() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toSchedulePage();
            SchedulePage schedulePage = new SchedulePage(driver, wait);
            schedulePage.switchViewToMonth();
            assertTrue(schedulePage.isMonthView());
        });
    }
}
