package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

abstract public class BasePage {

    WebDriver driver;
    WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public WebElement find(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        if (element == null) {
            throw new IllegalArgumentException("Element " + locator + " not found!");
        }
        if (!element.isDisplayed()) {
            throw new IllegalArgumentException("Element " + " is not displayed!");
        }
        return element;
    }

    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        find(locator).click();
    }

    public void set(By locator, String text) {
        find(locator).sendKeys(text);
    }

    protected boolean isVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)) != null;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
