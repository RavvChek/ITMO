package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ServicesPage extends BasePage {
    private final By map = By.xpath("//div[@id='map']");
    private final By dormitoryImg = By.xpath("//img[@src='/img/dorms/Belorusskaya.svg']");
    private final By mapLink = By.xpath("//a[@href='/services/navigator']");
    private final By dormitoriesLink = By.xpath("//a[@href='/services/dormitories']");
    private final By payButton = By.xpath("//button[contains(@class, 'btn btn-primary btn-sm')]");
    private final By payButtonInWindow = By.xpath("//button[@class='btn btn-primary']");
    private final By payWindowTitle = By.xpath("//button[contains(@class, 'btn sberPay-button')]");

    public ServicesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void toMapPage() {
        click(mapLink);
    }

    public void toDormitoriesPage() {
        click(dormitoriesLink);
    }

    public boolean isServicesPage() {
        return isVisible(mapLink) && isVisible(dormitoriesLink);
    }

    public boolean isMapPage() {
        return isVisible(map);
    }

    public boolean isDormitoryPage() {
        return isVisible(dormitoryImg);
    }

    public boolean isPayWindow() {
        return isVisible(payWindowTitle);
    }

    public void payDormitory() {
        click(payButton);
        click(payButtonInWindow);
    }

}
