package orsk.pages;

import core.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = DriverManager.getWait();
        PageFactory.initElements(driver, this);
    }

    protected void clickElement(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            System.out.println("Не удалось кликнуть на элемент: " + e.getMessage());
        }
    }

    protected void typeText(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element)).clear();
            element.sendKeys(text);
        } catch (Exception e) {
            System.out.println("Не удалось ввести текст в элемент: " + e.getMessage());
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPageLoaded() {
        return driver.getCurrentUrl().contains("orsk.ru");
    }
}