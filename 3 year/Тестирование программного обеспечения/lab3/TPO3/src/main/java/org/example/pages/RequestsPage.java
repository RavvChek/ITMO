package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RequestsPage extends BasePage {

    private final By linkRequest = By.xpath("//a[contains(@href, '/requests/new')]");
    private final By linkMyRequests = By.xpath("//a[@href='/requests/my']");
    private final By searchField = By.xpath("//div[@class='mr-3 ml-md-3 col']//input[contains(@class, 'form-control')]");
    private final By overlay = By.xpath("//div[@class='position-absolute rounded-16']");
    private final By buttonSendRequest = By.xpath("/html/body/div[2]/div[1]/div/div/footer/button");
    private final By searchButton = By.xpath("//div[@class='col-auto']//button");
    private final By invalidFeedback = By.xpath("//div[contains(@class, 'invalid-feedback')]");

    public RequestsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isRequestsPage() {
        return isVisible(linkMyRequests);
    }

    public void makeRequest() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchField));
        set(searchField, "справка с места");
        click(searchButton);
        click(linkRequest);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        wait.until(ExpectedConditions.visibilityOfElementLocated(buttonSendRequest));
        click(buttonSendRequest);
    }

    public boolean isInvalidFeedback() {
        return isVisible(invalidFeedback);
    }

}
