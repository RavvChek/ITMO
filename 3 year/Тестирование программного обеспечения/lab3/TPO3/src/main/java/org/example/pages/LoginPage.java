package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private final By usernameField = By.xpath("//input[@id='username']");
    private final By passportField = By.xpath("//input[@id='password']");

    private final By submitButton = By.xpath("//input[@id='kc-login']");
    private final By imgLogin = By.xpath("//img[contains(@class, 'mx-auto logo')]");
    private final By vkLink = By.xpath("//a[@id='social-vk']");
    private final By errorMessage = By.xpath("//span[@id='input-error']");
    private final By svgQRCode = By.xpath("//*[name()='svg' and contains(@class, 'vkc__QRCode__image')]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public By getQRCode() {
        return svgQRCode;
    }
    public void login(String username, String passport) {
        set(usernameField, username);
        set(passportField, passport);
        click(submitButton);
    }

    public void vkLogin() {
        click(vkLink);
    }

    public boolean isLoginPage() {
        return isVisible(imgLogin);
    }

    public boolean isErrorLoginOrPassword() {
        return isVisible(errorMessage);
    }

    public void resetForm() {
        find(usernameField).clear();
        find(passportField).clear();
    }

}
