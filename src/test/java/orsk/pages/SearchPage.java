package orsk.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class SearchPage extends BasePage {

    @FindBy(css = ".search-results, .results")
    private WebElement searchResults;

    @FindBy(css = ".result-item, .search-item")
    private List<WebElement> resultItems;

    @FindBy(css = ".no-results, .empty-results")
    private WebElement noResultsMessage;

    @FindBy(css = ".search-query, .query-text")
    private WebElement searchQueryText;

    @FindBy(css = ".result-count, .total-results")
    private WebElement resultCount;

    public boolean areResultsDisplayed() {
        return isElementDisplayed(searchResults);
    }

    public int getResultCount() {
        if (isElementDisplayed(resultCount)) {
            String text = resultCount.getText();
            // Extract number from text like "Найдено 15 результатов"
            return Integer.parseInt(text.replaceAll("\\D+", ""));
        }
        return resultItems.size();
    }

    public boolean isNoResultsMessageDisplayed() {
        return isElementDisplayed(noResultsMessage);
    }

    public String getSearchQuery() {
        return searchQueryText.getText();
    }

    public void clickFirstResult() {
        if (!resultItems.isEmpty()) {
            clickElement(resultItems.get(0));
        }
    }

    public List<WebElement> getAllResults() {
        return resultItems;
    }
}