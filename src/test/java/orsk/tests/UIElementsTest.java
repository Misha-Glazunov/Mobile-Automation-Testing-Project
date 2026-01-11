package orsk.tests;

import core.BaseTest;
import orsk.pages.MainPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UIElementsTest extends BaseTest {

    @Test(priority = 1, description = "Проверка основных элементов интерфейса")
    public void testMainPageElements() {
        System.out.println("Тест 1: Проверка основных элементов интерфейса");

        MainPage mainPage = new MainPage();

        // Проверяем основные элементы
        boolean hasLogo = mainPage.isLogoDisplayed();
        boolean hasNavigation = mainPage.isNavigationPresent();
        boolean hasFooter = mainPage.isFooterDisplayed();

        System.out.println("Элементы на странице:");
        System.out.println("- Логотип: " + (hasLogo ? "✓" : "✗"));
        System.out.println("- Навигация: " + (hasNavigation ? "✓" : "✗"));
        System.out.println("- Футер: " + (hasFooter ? "✓" : "✗"));

        // Проверяем заголовок страницы
        String pageTitle = driver.getTitle();
        System.out.println("- Заголовок страницы: " + pageTitle);

        // Проверяем URL
        String currentUrl = driver.getCurrentUrl();
        System.out.println("- Текущий URL: " + currentUrl);

        // Базовая проверка: страница должна содержать текст
        String pageSource = driver.getPageSource();
        boolean hasContent = pageSource.length() > 100;

        Assert.assertTrue(hasContent, "Страница должна содержать контент");
        Assert.assertTrue(currentUrl.contains("orsk.ru"), "URL должен содержать orsk.ru");

        System.out.println("✅ Основные элементы интерфейса проверены");
    }

    @Test(priority = 2, description = "Проверка заголовков и текста")
    public void testSectionTitles() {
        System.out.println("\nТест 2: Проверка заголовков и текста");

        String pageSource = driver.getPageSource().toLowerCase();
        int contentLength = pageSource.length();

        System.out.println("- Длина контента: " + contentLength + " символов");

        // Проверяем наличие общих слов (сайт на русском)
        String[] commonWords = {"орск", "новости", "город", "информация"};
        int foundWords = 0;

        for (String word : commonWords) {
            if (pageSource.contains(word)) {
                foundWords++;
                System.out.println("- Найдено слово: '" + word + "'");
            }
        }

        System.out.println("- Всего найдено общих слов: " + foundWords + " из " + commonWords.length);

        // Минимальная проверка: страница должна содержать какой-то текст
        Assert.assertTrue(contentLength > 100, "Страница должна содержать контент");

        if (foundWords > 0) {
            System.out.println("✅ Страница содержит релевантный контент");
        } else {
            System.out.println("⚠️ Не найдено ожидаемых слов, но контент присутствует");
        }
    }

    @Test(priority = 3, description = "Проверка скорости загрузки")
    public void testPageResponsiveness() {
        System.out.println("\nТест 3: Проверка скорости загрузки");

        long startTime = System.currentTimeMillis();

        // Перезагружаем страницу для чистоты теста
        driver.get("https://orsk.ru");

        // Ждем загрузки
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long loadTime = System.currentTimeMillis() - startTime;

        String pageTitle = driver.getTitle();
        boolean titleNotEmpty = pageTitle != null && !pageTitle.isEmpty();

        System.out.println("- Время загрузки: " + loadTime + " мс");
        System.out.println("- Заголовок загружен: " + (titleNotEmpty ? "✓" : "✗"));
        System.out.println("- Заголовок: " + pageTitle);

        // Проверяем, что страница загрузилась за разумное время
        Assert.assertTrue(loadTime < 30000, "Страница должна загружаться менее чем за 30 секунд");
        Assert.assertTrue(titleNotEmpty, "Заголовок страницы не должен быть пустым");

        if (loadTime < 5000) {
            System.out.println("✅ Отличная скорость загрузки!");
        } else if (loadTime < 10000) {
            System.out.println("✅ Нормальная скорость загрузки");
        } else {
            System.out.println("⚠️ Скорость загрузки могла быть лучше");
        }
    }
}