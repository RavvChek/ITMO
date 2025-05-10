package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FinancesPage extends BasePage{
    private final By overlay = By.xpath("//div[@class='position-absolute']");

    private final By titleFinancesPage = By.xpath("//div[contains(@class, 'card scholarship p-1')]");
    private final By arrowButton = By.xpath("//button[contains(@class, 'list-item__arrow-btn')]");
    private final By extendMonthScholar = By.xpath("//div[contains(@class, 'collapse show')]");


    public FinancesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isFinancesPage() {
        return isVisible(titleFinancesPage);
    }

    public void openMonthScholar() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        click(arrowButton);
    }

    public boolean isMonthScholar() {
        return isVisible(extendMonthScholar);
    }

}
