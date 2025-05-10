package tests;

import org.example.pages.LoginPage;
import org.example.pages.MainPage;
import org.example.pages.PersonsPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonsTest extends BaseTest{

    @Test
    public void isPersonsPageTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toPersonsPage();
            PersonsPage personsPage = new PersonsPage(driver, wait);
            assertTrue(personsPage.isPersonsPage());
        });
    }

    @Test
    public void searchPersonTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toPersonsPage();
            PersonsPage personsPage = new PersonsPage(driver, wait);
            personsPage.searchPerson();
            assertTrue(personsPage.isCardPerson());
        });
    }
}
