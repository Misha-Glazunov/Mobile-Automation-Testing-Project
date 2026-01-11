package wikipedia.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import wikipedia.pages.ArticlePage;
import wikipedia.pages.MainPage;
import wikipedia.pages.SearchPage;
import utils.ConfigReader;

import java.net.URL;
import java.time.Duration;

public class ArticleSearchTest {

    private AppiumDriver driver;
    private MainPage mainPage;
    private SearchPage searchPage;
    private ArticlePage articlePage;

    @BeforeClass
    public void setUp() throws Exception {
        // Настройка capabilities для Appium
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", ConfigReader.getPlatformName());
        capabilities.setCapability("deviceName", ConfigReader.getDeviceName());
        capabilities.setCapability("platformVersion", ConfigReader.getPlatformVersion());
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appPackage", ConfigReader.getAppPackage());
        capabilities.setCapability("appActivity", ConfigReader.getAppActivity());
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("fullReset", false);

        // Если используем предустановленное приложение, раскомментировать:
        // capabilities.setCapability("app", ConfigReader.getAppPath());

        // Инициализация драйвера
        URL appiumServerUrl = new URL(ConfigReader.getAppiumUrl());
        driver = new AndroidDriver(appiumServerUrl, capabilities);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Инициализация Page Objects
        mainPage = new MainPage(driver);
        searchPage = new SearchPage(driver);
        articlePage = new ArticlePage(driver);

        System.out.println("Appium драйвер инициализирован");
        System.out.println("Запущено приложение: " + driver.getCapabilities().getCapability("appPackage"));
    }

    @Test(priority = 1, description = "Проверка загрузки главного экрана")
    public void testMainScreenLoad() {
        // Проверяем, что главный экран загружен
        Assert.assertTrue(mainPage.isSearchBoxDisplayed(),
                "Поле поиска не отображается на главном экране");

        // Проверяем, что есть хотя бы одна статья на главной
        boolean hasFeaturedArticle = mainPage.isFeaturedArticleDisplayed();

        System.out.println("Главный экран загружен успешно");
        System.out.println("Есть избранная статья: " + hasFeaturedArticle);

        if (hasFeaturedArticle) {
            String featuredTitle = mainPage.getFeaturedArticleTitle();
            System.out.println("Заголовок избранной статьи: " + featuredTitle);
        }
    }

    @Test(priority = 2, description = "Поиск статьи по ключевому слову")
    public void testArticleSearch() {
        // Кликаем на поле поиска
        mainPage.clickSearchBox();

        // Проверяем, что открылась страница поиска
        Assert.assertTrue(searchPage.areResultsDisplayed() ||
                        driver.getPageSource().contains("search"),
                "Страница поиска не открылась");

        // Выполняем поиск
        String searchQuery = "Automation";
        searchPage.searchFor(searchQuery);

        // Ждем результаты
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем, что есть результаты
        int resultsCount = searchPage.getSearchResultsCount();
        Assert.assertTrue(resultsCount > 0,
                "Нет результатов поиска для запроса: " + searchQuery);

        System.out.println("Найдено результатов: " + resultsCount);

        // Получаем заголовок первого результата
        String firstResultTitle = searchPage.getFirstResultTitle();
        Assert.assertFalse(firstResultTitle.isEmpty(),
                "Заголовок первого результата пустой");

        System.out.println("Первый результат: " + firstResultTitle);
    }

    @Test(priority = 3, description = "Открытие и проверка статьи")
    public void testOpenAndVerifyArticle() {
        // Если мы на странице поиска, открываем первую статью
        searchPage.clickFirstResult();

        // Ждем загрузки статьи
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем, что статья открылась
        String articleTitle = articlePage.getArticleTitle();
        Assert.assertFalse(articleTitle.isEmpty(),
                "Заголовок статьи не загрузился");

        // Проверяем, что у статьи есть контент
        int paragraphCount = articlePage.getParagraphCount();
        Assert.assertTrue(paragraphCount > 0,
                "Статья не содержит текста");

        // Проверяем наличие изображения (опционально)
        boolean hasImage = articlePage.isArticleImageDisplayed();

        System.out.println("Статья открыта успешно:");
        System.out.println("- Заголовок: " + articleTitle);
        System.out.println("- Количество параграфов: " + paragraphCount);
        System.out.println("- Есть изображение: " + hasImage);

        // Проверяем подзаголовок (если есть)
        String subtitle = articlePage.getArticleSubtitle();
        if (!subtitle.isEmpty()) {
            System.out.println("- Подзаголовок: " + subtitle);
        }
    }

    @Test(priority = 4, description = "Поиск статьи на русском языке")
    public void testRussianArticleSearch() {
        // Возвращаемся на главный экран
        driver.navigate().back();
        driver.navigate().back();

        // Даем время для возврата
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Снова открываем поиск
        mainPage.clickSearchBox();

        // Ищем статью на русском
        searchPage.searchFor("Автоматизация");

        // Ждем результаты
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем результаты
        int resultsCount = searchPage.getSearchResultsCount();
        Assert.assertTrue(resultsCount > 0,
                "Нет результатов поиска на русском языке");

        System.out.println("Найдено русскоязычных статей: " + resultsCount);

        // Открываем первую статью
        searchPage.clickFirstResult();

        // Проверяем заголовок
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String russianTitle = articlePage.getArticleTitle();
        Assert.assertFalse(russianTitle.isEmpty(),
                "Русскоязычная статья не загрузилась");

        System.out.println("Открыта русскоязычная статья: " + russianTitle);

        // Проверяем, что текст на русском (хотя бы частично)
        // Для упрощения просто проверяем наличие кириллицы
        boolean hasCyrillic = russianTitle.matches(".*[А-Яа-я].*");
        Assert.assertTrue(hasCyrillic,
                "Заголовок статьи не содержит кириллицу: " + russianTitle);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Appium драйвер остановлен");
        }
    }
}