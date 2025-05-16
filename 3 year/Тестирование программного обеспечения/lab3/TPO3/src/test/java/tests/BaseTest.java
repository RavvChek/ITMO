package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

abstract public class BaseTest {
    protected static List<WebDriver> drivers;
    protected static final String URL = "https://my.itmo.ru/";

    @BeforeEach
    public void setUp() {
        drivers = new ArrayList<>();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--lang=ru");
        WebDriver chromeDriver = new ChromeDriver(chromeOptions);
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("intl.accept_languages", "ru");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(profile);
        WebDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
        chromeDriver.manage().window().maximize();
        firefoxDriver.manage().window().maximize();
        drivers.add(chromeDriver);
        drivers.add(firefoxDriver);
    }

    @AfterEach
    public void tearDown() {
        if (drivers != null) {
            drivers.forEach(WebDriver::quit);
        }
    }
}
