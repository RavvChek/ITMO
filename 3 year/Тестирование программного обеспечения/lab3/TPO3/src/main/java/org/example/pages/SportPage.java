package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SportPage extends BasePage{
    private final By overlay = By.xpath("div[@class='position-absolute']");

    private final By section = By.xpath("//a[@class='section-block']");
    private final By sectionWindow = By.xpath("//div[@role='tooltip']");

    private final By typeSportField = By.xpath("/html/body/div/div/div/div[1]/div/div[2]/div/div/div[3]/div/div/div[3]/div/div[2]/input");
    private final By teacherField = By.xpath("");

    private final By cardPoints = By.xpath("//div[contains(@class, 'card my-points')]");
    public SportPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isSportPage() {
        return isVisible(cardPoints);
    }

    public void signOnSectionSPort() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(section));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        click(section);
    }

    public boolean isWindowSection() {
        return isVisible(sectionWindow);
    }
}
