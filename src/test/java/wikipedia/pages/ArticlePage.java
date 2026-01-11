package wikipedia.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ArticlePage extends BaseMobilePage {

    @AndroidFindBy(id = "org.wikipedia:id/view_page_title_text")
    @iOSXCUITFindBy(accessibility = "articleTitle")
    private WebElement articleTitle;

    @AndroidFindBy(id = "org.wikipedia:id/view_page_subtitle_text")
    private WebElement articleSubtitle;

    @AndroidFindBy(className = "android.widget.TextView")
    private List<WebElement> articleParagraphs;

    @AndroidFindBy(accessibility = "More options")
    private WebElement moreOptionsButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='See also']")
    private WebElement seeAlsoSection;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='References']")
    private WebElement referencesSection;

    @AndroidFindBy(xpath = "//*[contains(@text, 'See also')]")
    private WebElement seeAlsoSectionAlt;

    @AndroidFindBy(xpath = "//*[contains(@text, 'References')]")
    private WebElement referencesSectionAlt;

    @AndroidFindBy(xpath = "//*[contains(@text, 'Примечания')]")
    private WebElement notesSection;

    @AndroidFindBy(xpath = "//*[contains(@text, 'Литература')]")
    private WebElement literatureSection;

    @AndroidFindBy(id = "org.wikipedia:id/view_article_header_image")
    private WebElement articleImage;

    public ArticlePage(AppiumDriver driver) {
        super(driver);
    }

    public String getArticleTitle() {
        return getElementText(articleTitle);
    }

    public String getArticleSubtitle() {
        return getElementText(articleSubtitle);
    }

    public int getParagraphCount() {
        return articleParagraphs.size();
    }

    public boolean isArticleImageDisplayed() {
        return isElementDisplayed(articleImage);
    }

    // Улучшенный метод для поиска раздела
    public boolean findSection(String sectionName) {
        int maxAttempts = 20;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                // Пробуем разные локаторы
                List<WebElement> elements = driver.findElements(By.xpath(
                        "//*[contains(@text, '" + sectionName + "')]"));

                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    System.out.println("Раздел '" + sectionName + "' найден после " + i + " прокруток");
                    return true;
                }

                // Также проверяем по атрибуту content-desc
                elements = driver.findElements(By.xpath(
                        "//*[contains(@content-desc, '" + sectionName + "')]"));

                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                    System.out.println("Раздел '" + sectionName + "' найден по content-desc");
                    return true;
                }
            } catch (Exception e) {
                // Игнорируем исключения, продолжаем скроллить
            }

            // Скроллим вниз
            scrollDown();

            // Небольшая пауза
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Проверяем, не достигли ли мы конца
            if (isAtBottom()) {
                System.out.println("Достигнут конец статьи");
                break;
            }
        }

        System.out.println("Раздел '" + sectionName + "' не найден после " + maxAttempts + " попыток");
        return false;
    }

    public void scrollToSeeAlso() {
        if (findSection("See also")) {
            System.out.println("✅ Раздел 'See also' найден");
        } else {
            System.out.println("⚠️ Раздел 'See also' не найден в этой статье");
        }
    }

    public void scrollToReferences() {
        if (findSection("References")) {
            System.out.println("✅ Раздел 'References' найден");
        } else if (findSection("Примечания")) {
            System.out.println("✅ Раздел 'Примечания' найден");
        } else if (findSection("Литература")) {
            System.out.println("✅ Раздел 'Литература' найден");
        } else {
            System.out.println("⚠️ Разделы с примечаниями не найдены");
        }
    }

    public boolean isSeeAlsoSectionDisplayed() {
        try {
            return seeAlsoSection.isDisplayed() || seeAlsoSectionAlt.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReferencesSectionDisplayed() {
        try {
            return referencesSection.isDisplayed() || referencesSectionAlt.isDisplayed() ||
                    notesSection.isDisplayed() || literatureSection.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Метод для проверки, достигли ли мы конца статьи
    private boolean isAtBottom() {
        try {
            // Проверяем наличие футера или кнопки "Наверх"
            return driver.findElement(By.xpath("//*[contains(@text, 'Наверх')]")).isDisplayed() ||
                    driver.findElement(By.xpath("//*[contains(@text, 'Back to top')]")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Метод для скроллинга до конца статьи
    public void scrollToBottom() {
        System.out.println("Начинаем скроллинг до конца статьи...");
        for (int i = 0; i < 15; i++) {
            scrollDown();
            try {
                Thread.sleep(300);
                if (isAtBottom()) {
                    System.out.println("Достигнут конец статьи после " + i + " скроллов");
                    return;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Завершено 15 скроллов");
    }

    // Метод для поиска текста в видимых элементах
    public boolean isTextVisible(String text) {
        String pageSource = driver.getPageSource();
        return pageSource.contains(text);
    }
}