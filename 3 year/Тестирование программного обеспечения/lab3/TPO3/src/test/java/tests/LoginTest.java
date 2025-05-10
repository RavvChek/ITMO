package tests;

import org.example.pages.MainPage;
import org.junit.jupiter.api.Test;
import org.example.pages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest extends BaseTest {

    @Test
    public void loginWithLoginAndPasswordTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            LoginPage loginPage = new LoginPage(driver, wait);
            assertTrue(loginPage.isLoginPage(), "Не получилось перейти на страницу авторизации!");
            loginPage.login(username, password);
            MainPage mainPage = new MainPage(driver, wait);
            assertTrue(mainPage.isMainPage(), "Не получилось залогиниться!");
        });
    }

    @Test
    public void loginViaSocialNetworksTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.vkLogin();
            MainPage mainPage = new MainPage(driver, wait);
            WebDriverWait w = new WebDriverWait(driver, Duration.ofMinutes(3));
            w.until(ExpectedConditions.urlContains("my.itmo.ru"));
            assertTrue(mainPage.isMainPage(), "Не получилось залогиниться через Вк");
        });
    }


    @Test
    public void loginWithWrongPassportAndLoginTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            LoginPage loginPage = new LoginPage(driver, wait);
            MainPage mainPage = new MainPage(driver, wait);
            loginPage.login(username, "wrong_password");
            assertTrue(loginPage.isErrorLoginOrPassword(), "Сообщение об ошибке не появилось");
            assertFalse(mainPage.isMainPage(), "Мы перешли на страницу, хотя не должны были");
            loginPage.resetForm();
            loginPage.login("wrong_login", password);
            assertTrue(loginPage.isErrorLoginOrPassword(), "Сообщение об ошибке не появилось");
            assertFalse(mainPage.isMainPage(), "Мы перешли на страницу, хотя не должны были");
            loginPage.resetForm();
            loginPage.login("wrong_login", "wrong_password");
            assertTrue(loginPage.isErrorLoginOrPassword(), "Сообщение об ошибке не появилось");
            assertFalse(mainPage.isMainPage(), "Мы перешли на страницу, хотя не должны были");
        });
    }

    @Test
    public void logoutTest() {
        drivers.parallelStream().forEach(driver -> {
            driver.get(URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            MainPage mainPage = new MainPage(driver, wait);
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(username, password);
            mainPage.logout();
            assertTrue(loginPage.isLoginPage());
        });
    }
}