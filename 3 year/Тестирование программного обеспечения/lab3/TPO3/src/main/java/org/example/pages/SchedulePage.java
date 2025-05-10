package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SchedulePage extends BasePage {
    private final By rightArrowButton = By.xpath("/html/body/div/div/div/div[1]/div/div[2]/div/div/div/div/div/div/div[1]/div[1]/div/div[1]/button[2]");
    private final By leftArrowButton = By.xpath("//*[@id=\"__layout\"]/div/div[1]/div/div[2]/div/div/div/div/div/div/div[1]/div[1]/div/div[1]/button[1]");
    private final By weekButton = By.xpath("/html/body/div/div/div/div[1]/div/div[2]/div/div/div/div/div/div/div[1]/div[2]/div/label[1]/span");
    private final By monthButton = By.xpath("//*[@id=\"btn-radios-1\"]/label[2]");
    private final By weekName = By.xpath("//div[@class='week-name']");
    private final By overlay = By.xpath("//div[@class='position-absolute']");


    public SchedulePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void switchViewToMonth() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        click(monthButton);
    }


    public void switchViewToWeek() {
        click(weekButton);
    }

    public void nextWeekOrNextMonth() {
        click(rightArrowButton);
    }

    public void prevWeekOrMonthButton() {
        click(leftArrowButton);
    }

    public boolean isMonthView() {

        return isVisible(weekName);
    }
}
