package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PersonsPage extends BasePage {

    private final By searchButton = By.xpath("/html/body/div/div/div/div[1]/div/div[2]/div/div/div/div[1]/div[1]/div[2]/button");
    private final By searchField = By.xpath("/html/body/div/div/div/div[1]/div/div[2]/div/div/div/div[1]/div[1]/div[1]/input");
    private final By cardPerson = By.xpath("/html/body/div/div/div/div[1]/div/div[2]/div/div/div/div[1]/div[3]/div/a");

    private final By personalitiesPhoto = By.xpath("//div[@class='personalities__photo']");

    private final By overlay = By.xpath("//div[@class='position-absolute']");

    public PersonsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isPersonsPage() {
        return isVisible(cardPerson);
    }

    public void searchPerson() {
        set(searchField, "Бострикова Дарья Константиновна");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        click(searchButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        wait.until(ExpectedConditions.visibilityOfElementLocated(cardPerson));
        click(cardPerson);
    }

    public boolean isCardPerson() {
        return isVisible(personalitiesPhoto);
    }
}
