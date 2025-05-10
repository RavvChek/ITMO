package tests;

import org.example.pages.FinancesPage;
import org.example.pages.LoginPage;
import org.example.pages.MainPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinancesTest extends BaseTest{

    @Test
    public void viewScholarshipTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            FinancesPage financesPage = new FinancesPage(driver, wait);
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toFinancesPage();
            assertTrue(financesPage.isFinancesPage());
        });
    }

    @Test
    public void viewScholarshipOnMonthTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            FinancesPage financesPage = new FinancesPage(driver, wait);
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toFinancesPage();
            financesPage.openMonthScholar();
            assertTrue(financesPage.isMonthScholar());
        });
    }
}
