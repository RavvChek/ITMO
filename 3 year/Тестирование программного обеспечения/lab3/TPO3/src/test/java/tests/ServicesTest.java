package tests;

import org.example.pages.LoginPage;
import org.example.pages.MainPage;
import org.example.pages.ServicesPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServicesTest extends BaseTest {

    @Test
    public void viewListOfServicesTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toServicesPage();
            ServicesPage servicesPage = new ServicesPage(driver, wait);
            assertTrue(servicesPage.isServicesPage());
        });
    }

    @Test
    public void viewMapTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toServicesPage();
            ServicesPage servicesPage = new ServicesPage(driver, wait);
            servicesPage.toMapPage();
            assertTrue(servicesPage.isMapPage());
        });
    }

    @Test
    public void viewDormitoryTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toServicesPage();
            ServicesPage servicesPage = new ServicesPage(driver, wait);
            servicesPage.toDormitoriesPage();
            assertTrue(servicesPage.isDormitoryPage());
        });
    }

    @Test
    public void payDormitoryTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.toServicesPage();
            ServicesPage servicesPage = new ServicesPage(driver, wait);
            servicesPage.toDormitoriesPage();
            servicesPage.payDormitory();
            assertTrue(servicesPage.isPayWindow());
        });
    }

}
