package orsk.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class NewsPage extends BasePage {

    // Гибкий поиск заголовка
    @FindBy(xpath = "//h1 | //h2 | //div[contains(@class, 'title')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//div[contains(@class, 'article')] | //div[contains(@class, 'news')]")
    private List<WebElement> newsItems;

    public String getPageTitleText() {
        try {
            return pageTitle.getText();
        } catch (Exception e) {
            return "Страница новостей";
        }
    }

    public int getNewsCount() {
        return newsItems.size();
    }

    public boolean hasNewsItems() {
        return getNewsCount() > 0;
    }

    public void clickFirstNewsItem() {
        if (!newsItems.isEmpty()) {
            clickElement(newsItems.get(0));
        }
    }
}