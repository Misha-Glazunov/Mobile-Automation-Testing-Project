package wikipedia.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class MainPage extends BaseMobilePage {

    @AndroidFindBy(id = "org.wikipedia:id/search_container")
    @iOSXCUITFindBy(accessibility = "Search Wikipedia")
    private WebElement searchBox;

    @AndroidFindBy(id = "org.wikipedia:id/view_card_header_title")
    @iOSXCUITFindBy(accessibility = "Featured article")
    private WebElement featuredArticleTitle;

    @AndroidFindBy(id = "org.wikipedia:id/view_announcement_text")
    private WebElement announcementText;

    @AndroidFindBy(accessibility = "Navigate up")
    private WebElement navigationButton;

    @AndroidFindBy(id = "org.wikipedia:id/nav_more_container")
    private WebElement moreOptionsButton;

    public MainPage(AppiumDriver driver) {
        super(driver);
    }

    public void clickSearchBox() {
        clickElement(searchBox);
    }

    public boolean isSearchBoxDisplayed() {
        return isElementDisplayed(searchBox);
    }

    public boolean isFeaturedArticleDisplayed() {
        return isElementDisplayed(featuredArticleTitle);
    }

    public String getFeaturedArticleTitle() {
        return getElementText(featuredArticleTitle);
    }

    public void openNavigationMenu() {
        clickElement(navigationButton);
    }
}