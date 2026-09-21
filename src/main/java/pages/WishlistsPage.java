package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Page Object для страницы со списками желаний.
 */
public class WishlistsPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(WishlistsPage.class);

    private final By createListBtn = By.xpath("//button[contains(@class, 'btn-primary') and contains(., 'Создать новый список')]");
    private final By modalContent = By.className("modal-content");
    private final By modalTitleInput = By.xpath("//label[text()='Название']/following-sibling::input");
    private final By modalDescInput = By.xpath("//label[text()='Описание (необязательно)']/following-sibling::textarea");
    private final By modalSubmitBtn = By.xpath("//button[@type='submit' and contains(., 'Создать')]");

    public WishlistsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Создает новый список желаний через модальное окно.
     * @param title название списка
     * @param description описание
     */
    public void createNewList(String title, String description) {
        logger.info("Создание списка: {}", title);
        click(createListBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalContent));

        type(modalTitleInput, title);
        type(modalDescInput, description);
        click(modalSubmitBtn);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalContent));
    }

    /**
     * Открывает детали списка по его названию и ждет загрузки страницы.
     * @param listTitle название списка
     */
    public void openWishlistDetails(String listTitle) {
        logger.info("Открытие списка: {}", listTitle);
        String xpath = String.format(
                "//div[contains(@class, 'card') and .//div[contains(@class, 'card-title') and contains(., '%s')]]//button[contains(., 'Просмотр')]",
                listTitle
        );
        WebElement viewBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", viewBtn);
        viewBtn.click();

        // === ИСПРАВЛЕНИЕ: Явно ждем загрузки страницы деталей ===
        By detailsHeader = By.xpath("//h2[contains(., '" + listTitle + "')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(detailsHeader));
        logger.info("Страница деталей списка успешно загружена");
    }
}