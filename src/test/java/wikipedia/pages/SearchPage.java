package wikipedia.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPage extends BaseMobilePage {

    @AndroidFindBy(id = "org.wikipedia:id/search_src_text")
    @iOSXCUITFindBy(accessibility = "Search Wikipedia")
    private WebElement searchInput;

    @AndroidFindBy(id = "org.wikipedia:id/page_list_item_title")
    @iOSXCUITFindBy(accessibility = "articleTitle")
    private List<WebElement> articleTitles;

    @AndroidFindBy(id = "org.wikipedia:id/page_list_item_description")
    private List<WebElement> articleDescriptions;

    @AndroidFindBy(id = "org.wikipedia:id/search_empty_view")
    private WebElement noResultsView;

    public SearchPage(AppiumDriver driver) {
        super(driver);
    }

    public void searchFor(String query) {
        typeText(searchInput, query);
    }

    public int getSearchResultsCount() {
        return articleTitles.size();
    }

    public void clickFirstResult() {
        if (!articleTitles.isEmpty()) {
            clickElement(articleTitles.get(0));
        }
    }

    public String getFirstResultTitle() {
        if (!articleTitles.isEmpty()) {
            return getElementText(articleTitles.get(0));
        }
        return "";
    }

    public boolean areResultsDisplayed() {
        return !articleTitles.isEmpty();
    }

    public void clearSearch() {
        try {
            // Для Android
            driver.findElement(By.id("org.wikipedia:id/search_close_btn")).click();
        } catch (Exception e) {
            // Для iOS или если не найден
            searchInput.clear();
        }
    }

    public boolean isNoResultsDisplayed() {
        try {
            return noResultsView.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}