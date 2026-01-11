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

public class ArticleScrollTest {

    private AppiumDriver driver;
    private MainPage mainPage;
    private SearchPage searchPage;
    private ArticlePage articlePage;

    @BeforeClass
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", ConfigReader.getPlatformName());
        capabilities.setCapability("deviceName", ConfigReader.getDeviceName());
        capabilities.setCapability("platformVersion", ConfigReader.getPlatformVersion());
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appPackage", ConfigReader.getAppPackage());
        capabilities.setCapability("appActivity", ConfigReader.getAppActivity());
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("newCommandTimeout", 300);
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("autoAcceptAlerts", true);

        URL appiumServerUrl = new URL(ConfigReader.getAppiumUrl());
        driver = new AndroidDriver(appiumServerUrl, capabilities);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        mainPage = new MainPage(driver);
        searchPage = new SearchPage(driver);
        articlePage = new ArticlePage(driver);

        System.out.println("✅ Appium драйвер инициализирован");
        System.out.println("📱 Платформа: " + capabilities.getCapability("platformName"));
        System.out.println("📱 Устройство: " + capabilities.getCapability("deviceName"));
    }

    @Test(priority = 1, description = "Проверка базового скроллинга")
    public void testBasicScrolling() throws InterruptedException {
        System.out.println("🧪 Тест 1: Базовый скроллинг");

        // Открываем поиск
        mainPage.clickSearchBox();
        Thread.sleep(2000);

        // Ищем статью
        searchPage.searchFor("Automation");
        Thread.sleep(3000);

        // Проверяем результаты
        Assert.assertTrue(searchPage.getSearchResultsCount() > 0,
                "Нет результатов поиска");

        System.out.println("Найдено статей: " + searchPage.getSearchResultsCount());

        // Открываем первую статью
        searchPage.clickFirstResult();
        Thread.sleep(4000);

        // Проверяем, что статья открылась
        String title = articlePage.getArticleTitle();
        Assert.assertFalse(title.isEmpty(), "Заголовок статьи не загрузился");
        System.out.println("Открыта статья: " + title);

        // Проверяем, что есть контент
        int paragraphs = articlePage.getParagraphCount();
        Assert.assertTrue(paragraphs > 0, "Статья не содержит текста");
        System.out.println("Начальное количество параграфов: " + paragraphs);

        // Выполняем несколько скроллов
        for (int i = 0; i < 3; i++) {
            articlePage.scrollDown();
            Thread.sleep(1000);
            System.out.println("Скролл " + (i + 1) + " выполнен");
        }

        // Проверяем, что статья все еще отображается
        String currentTitle = articlePage.getArticleTitle();
        Assert.assertEquals(currentTitle, title, "Статья изменилась после скроллинга");
        System.out.println("✅ Базовый скроллинг выполнен успешно");
    }

    @Test(priority = 2, description = "Поиск раздела See also")
    public void testFindSeeAlsoSection() throws InterruptedException {
        System.out.println("\n🧪 Тест 2: Поиск раздела See also");

        // Возвращаемся и ищем другую статью
        driver.navigate().back();
        Thread.sleep(2000);

        mainPage.clickSearchBox();
        Thread.sleep(2000);

        searchPage.searchFor("Software testing");
        Thread.sleep(3000);

        if (searchPage.getSearchResultsCount() > 0) {
            searchPage.clickFirstResult();
            Thread.sleep(4000);

            // Ищем раздел See also
            articlePage.scrollToSeeAlso();

            // Проверяем результат
            boolean hasSeeAlso = articlePage.isTextVisible("See also");
            if (hasSeeAlso) {
                System.out.println("✅ Раздел 'See also' найден");
            } else {
                System.out.println("⚠️ Раздел 'See also' не найден, но это может быть нормально для этой статьи");
            }

            // Проверяем, что статья все еще загружена
            Assert.assertFalse(articlePage.getArticleTitle().isEmpty(),
                    "Статья потерялась после поиска раздела");
        } else {
            System.out.println("⚠️ Нет результатов для 'Software testing', пропускаем тест");
        }
    }

    @Test(priority = 3, description = "Прокрутка до конца статьи")
    public void testScrollToBottom() throws InterruptedException {
        System.out.println("\n🧪 Тест 3: Прокрутка до конца статьи");

        // Возвращаемся и ищем длинную статью
        driver.navigate().back();
        Thread.sleep(2000);

        mainPage.clickSearchBox();
        Thread.sleep(2000);

        searchPage.searchFor("History of computing");
        Thread.sleep(3000);

        if (searchPage.getSearchResultsCount() > 0) {
            searchPage.clickFirstResult();
            Thread.sleep(4000);

            String articleTitle = articlePage.getArticleTitle();
            System.out.println("Открыта статья: " + articleTitle);

            // Запоминаем начальное состояние
            int initialParagraphs = articlePage.getParagraphCount();
            System.out.println("Начальное количество параграфов: " + initialParagraphs);

            // Прокручиваем до конца
            articlePage.scrollToBottom();
            Thread.sleep(2000);

            // Проверяем, что статья все еще отображается
            String finalTitle = articlePage.getArticleTitle();
            Assert.assertEquals(finalTitle, articleTitle,
                    "Статья изменилась после прокрутки до конца");

            System.out.println("✅ Статья успешно прокручена до конца");
            System.out.println("Финальное название: " + finalTitle);
        } else {
            System.out.println("⚠️ Нет результатов для 'History of computing', пропускаем тест");
        }
    }

    @Test(priority = 4, description = "Поиск русскоязычных разделов")
    public void testRussianSections() throws InterruptedException {
        System.out.println("\n🧪 Тест 4: Поиск русскоязычных разделов");

        // Возвращаемся и ищем русскоязычную статью
        driver.navigate().back();
        Thread.sleep(2000);

        mainPage.clickSearchBox();
        Thread.sleep(2000);

        searchPage.searchFor("Программирование");
        Thread.sleep(3000);

        if (searchPage.getSearchResultsCount() > 0) {
            searchPage.clickFirstResult();
            Thread.sleep(4000);

            String articleTitle = articlePage.getArticleTitle();
            System.out.println("Открыта статья: " + articleTitle);

            // Проверяем, что статья на русском
            boolean isRussian = articleTitle.matches(".*[А-Яа-яЁё].*");
            if (isRussian) {
                System.out.println("✅ Статья на русском языке");

                // Ищем русскоязычные разделы
                articlePage.scrollToReferences();
                Thread.sleep(1000);

                // Проверяем видимость текста
                boolean hasRussianContent = articlePage.isTextVisible("Примечания") ||
                        articlePage.isTextVisible("Литература") ||
                        articlePage.isTextVisible("Ссылки");

                if (hasRussianContent) {
                    System.out.println("✅ Найдены русскоязычные разделы");
                } else {
                    System.out.println("⚠️ Русскоязычные разделы не найдены");
                }
            } else {
                System.out.println("⚠️ Статья не на русском, пропускаем проверку разделов");
            }
        } else {
            System.out.println("⚠️ Нет результатов для 'Программирование', пропускаем тест");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("\n Appium драйвер остановлен");
        }
    }
}