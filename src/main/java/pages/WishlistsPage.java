package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WishlistsPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(WishlistsPage.class);

    // Правильный локатор под реальный HTML
    private final By createListBtn = By.xpath("//button[contains(@class, 'btn-primary') and contains(., 'Создать новый список')]");
    private final By modalContent = By.className("modal-content");
    private final By modalTitleInput = By.xpath("//label[text()='Название']/following-sibling::input");
    private final By modalDescInput = By.xpath("//label[contains(., 'Описание')]/following-sibling::textarea");
    private final By modalSubmitBtn = By.xpath("//button[@type='submit' and contains(., 'Создать')]");

    public WishlistsPage(WebDriver driver) {
        super(driver);
    }

    public void createNewList(String title, String description) {
        logger.info("Создание списка: {}", title);
        click(createListBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalContent));

        type(modalTitleInput, title);
        type(modalDescInput, description);
        click(modalSubmitBtn);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalContent));
    }

    public void openWishlistDetails(String listTitle) {
        logger.info("Открытие списка: {}", listTitle);
        String xpath = String.format(
                "//div[contains(@class, 'card') and .//div[contains(@class, 'card-title') and contains(., '%s')]]//button[contains(., 'Просмотр')]",
                listTitle
        );
        WebElement viewBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", viewBtn);
        viewBtn.click();

        By detailsHeader = By.xpath("//h2[contains(., '" + listTitle + "')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(detailsHeader));
        logger.info("Страница деталей списка успешно загружена");
    }

    // Методы-ассерты (вынесены из тестов по требованию ментора)
    public void assertWishlistExists(String title) {
        if (!driver.getPageSource().contains(title)) {
            throw new AssertionError("Список '" + title + "' должен присутствовать на странице");
        }
    }

    public void assertWishlistDetailsOpened(String title) {
        if (!driver.getPageSource().contains(title)) {
            throw new AssertionError("Должны находиться на странице деталей списка '" + title + "'");
        }
    }
}