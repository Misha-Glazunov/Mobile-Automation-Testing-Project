package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = DriverManager.getDriver();
        wait = DriverManager.getWait();
        driver.get("https://orsk.ru");
    }

    @AfterMethod
    public void tearDown() {
        // Очищаем куки, но не закрываем драйвер полностью
        driver.manage().deleteAllCookies();
    }
}