package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PointsPage extends BasePage {

    private final By majorButton = By.xpath("/html/body/div/div/div/div[1]/div/div[2]/div/div/div/div[1]/div/div[1]/button");
    private final By imgInSubject = By.xpath("//img[@src='/img/points/no-details.svg']");
    private final By buttonExtendListOfSubject = By.xpath("//button[contains(@class, 'list-item__arrow-btn')]");
    private final By switcherSemesters = By.xpath("//div[contains(@class, 'record-book')]//div[contains(@class, 'ml-md-3')]//button");
    private final By grade = By.xpath("//h5//button[@class='list-item__grade list-item__grade_good']");


    public PointsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isPointsPage() {
        return isVisible(majorButton);
    }

    public void extendSubject() {
        click(buttonExtendListOfSubject);
    }

    public boolean isExtendedSubject() {
        return isVisible(imgInSubject);
    }

    public void switchSemester(int numberOfSemester) {
        click(switcherSemesters);
        List<WebElement> listOfSemesters = driver.findElements(By.xpath("//li"));
        listOfSemesters.get(listOfSemesters.size() - numberOfSemester).click();
    }

    public boolean isSwitchSemester() {
        return isVisible(grade);
    }
}
