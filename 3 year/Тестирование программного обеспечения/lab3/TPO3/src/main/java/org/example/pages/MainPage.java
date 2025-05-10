package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage extends BasePage {
    private final By scheduleLink = By.xpath("//a[@href='/schedule']");
    private final By pointsLink = By.xpath("//a[@href='/points']");
    private final By financesLink = By.xpath("//a[@href='/finances']");

    private final By titleMainPage = By.xpath("//div[contains(@class, 'dashboard')]");
    private final By  profileExtendButton= By.xpath("//a[@id='__BVID__41__BV_toggle_']");
    private final By logoutButton = By.xpath("//a[contains(@class, 'dropdown-item') and @href='#']");

    private final By extendSportButton = By.xpath("(//a[contains(@class, 'sidenav-link sidenav-toggle')])[2]");
    private final By sportLink = By.xpath("//a[@href='/sport']");
    private final By servicesLink = By.xpath("//a[@href='/services']");
    private final By personsLink = By.xpath("//a[@href='/persons']");
    private final By requestsLink = By.xpath("//a[@href='/requests']");
    private final By sportSectionLink = By.xpath("//a[@href='/sport/sign']");


    public MainPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void toSchedulePage() {
        click(scheduleLink);
    }

    public void toPointsPage() {
        click(pointsLink);
    }

    public void toFinancesPage() {
        click(financesLink);
    }

    public boolean isMainPage() {
        return isVisible(titleMainPage);
    }

    public void logout() {
        click(profileExtendButton);
        click(logoutButton);
    }

    public void toSportPoints() {
        click(extendSportButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(sportLink));
        click(sportLink);
    }

    public void toServicesPage() {
        click(servicesLink);
    }

    public void toRequestsPage() {
        click(requestsLink);
    }

    public void toPersonsPage() {
        click(personsLink);
    }

    public void toSectionSportPage() {
        click(extendSportButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(sportSectionLink));
        click(sportSectionLink);
    }
}
