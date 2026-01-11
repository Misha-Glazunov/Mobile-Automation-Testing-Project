package wikipedia.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;

public class BaseMobilePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    public BaseMobilePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    protected void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void typeText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).clear();
        element.sendKeys(text);
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected String getElementText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText();
    }

    // Исправленный метод скроллинга для Appium 8.x
    public void scrollDown() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.7);
            int endY = (int) (size.height * 0.3);

            // Используем W3C Actions вместо устаревшего TouchAction
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 0);

            scroll.addAction(finger.createPointerMove(Duration.ZERO,
                    PointerInput.Origin.viewport(), startX, startY));
            scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            scroll.addAction(finger.createPointerMove(Duration.ofMillis(500),
                    PointerInput.Origin.viewport(), startX, endY));
            scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Arrays.asList(scroll));
        } catch (Exception e) {
            System.out.println("Ошибка при скроллинге: " + e.getMessage());
        }
    }

    // Альтернативный метод скроллинга с использованием JavaScript
    protected void scrollDownJS() {
        try {
            // Для Android используем UiScrollable через executeScript
            if (driver instanceof AndroidDriver) {
                driver.executeScript("mobile: scrollGesture",
                        Arrays.asList(
                                "left", 100, "top", 100, "width", driver.manage().window().getSize().width - 200,
                                "height", driver.manage().window().getSize().height - 200,
                                "direction", "down",
                                "percent", 1.0
                        ));
            }
        } catch (Exception e) {
            System.out.println("JS скроллинг не сработал: " + e.getMessage());
        }
    }

    // Метод для скроллинга до элемента по тексту
    protected void scrollToElementByText(String text) {
        try {
            // Пытаемся найти элемент
            WebElement element = driver.findElement(By.xpath(
                    "//*[contains(@text, '" + text + "')]"));

            // Скроллим до него
            scrollToElement(element);
        } catch (Exception e) {
            System.out.println("Элемент с текстом '" + text + "' не найден");
        }
    }

    // Универсальный метод скроллинга до элемента
    protected void scrollToElement(WebElement element) {
        int maxAttempts = 15;
        for (int i = 0; i < maxAttempts; i++) {
            try {
                if (element.isDisplayed()) {
                    return;
                }
            } catch (Exception e) {
                // Элемент не виден, скроллим
                scrollDown();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println("Элемент не найден после " + maxAttempts + " попыток скроллинга");
    }

    // Метод для скроллинга с использованием UiScrollable (только Android)
    protected void scrollUsingUiScrollable(String textToFind) {
        if (driver instanceof AndroidDriver) {
            String uiScrollableCommand = String.format(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().textContains(\"%s\"))",
                    textToFind);

            try {
                driver.findElement(By.id("android:id/content"));
                driver.executeScript("mobile: scrollGesture", Arrays.asList(
                        "left", 100, "top", 100,
                        "width", driver.manage().window().getSize().width - 200,
                        "height", driver.manage().window().getSize().height - 200,
                        "direction", "down",
                        "percent", 3.0
                ));
            } catch (Exception e) {
                System.out.println("UiScrollable не сработал: " + e.getMessage());
            }
        }
    }
}