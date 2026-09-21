package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

/**
 * Page Object для страницы деталей списка желаний.
 */
public class WishlistDetailsPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(WishlistDetailsPage.class);

    private final By addGiftBtn = By.xpath("//button[contains(., 'Добавить подарок')]");
    private final By giftModal = By.xpath("//div[contains(@class, 'modal-title') and contains(., 'Добавить подарок')]/ancestor::div[contains(@class, 'modal-content')]");

    // Поля формы
    private final By giftNameInput = By.xpath("//label[text()='Название']/following-sibling::input");
    private final By giftDescInput = By.xpath("//label[text()='Описание']/following-sibling::textarea");
    private final By storeLinkInput = By.xpath("//label[contains(., 'Ссылка на магазин')]/following-sibling::input");
    private final By priceInput = By.xpath("//label[contains(., 'Цена')]/following-sibling::input");
    private final By imageLinkInput = By.xpath("//label[contains(., 'Ссылка на изображение')]/following-sibling::input");
    private final By giftSubmitBtn = By.xpath("//button[@type='submit' and contains(., 'Добавить')]");

    public WishlistDetailsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Добавляет подарок в список. Заполняет все поля (включая "необязательные"),
     * чтобы избежать багов валидации на стороне сервера учебного приложения.
     * @param name название подарка
     * @param description описание
     */
    public void addGift(String name, String description) {
        logger.info("Добавление подарка: {}", name);
        click(addGiftBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(giftModal));

        // Заполняем основные поля
        type(giftNameInput, name);
        type(giftDescInput, description);

        // Заполняем "необязательные" поля валидными данными для обхода багов бэкенда
        type(storeLinkInput, "https://example.com/product");
        type(priceInput, "100");
        type(imageLinkInput, "https://example.com/image.jpg");

        click(giftSubmitBtn);

        // 1. Ждем закрытия модального окна
        wait.until(ExpectedConditions.invisibilityOfElementLocated(giftModal));

        // 2. Локаторы для проверки результата
        By errorLocator = By.xpath("//*[contains(., 'Не удалось добавить подарок')]");
        By successLocator = By.xpath("//*[contains(., '" + name + "')]");

        // 3. Проверяем, не вылезла ли ошибка
        List<WebElement> errors = driver.findElements(errorLocator);
        if (!errors.isEmpty() && errors.get(0).isDisplayed()) {
            throw new AssertionError("Сбой на стороне приложения: '" + errors.get(0).getText().trim() + "'. " +
                    "Это не ошибка автотеста, а нестабильность сервера.");
        }

        // 4. Если ошибки нет, ждем появления подарка на странице
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successLocator));
            logger.info("Подарок '{}' успешно добавлен и отображается на странице", name);
        } catch (TimeoutException e) {
            // 5. Финальная перепроверка на ошибку, вдруг сервер ответил с задержкой
            errors = driver.findElements(errorLocator);
            if (!errors.isEmpty() && errors.get(0).isDisplayed()) {
                throw new AssertionError("Сбой на стороне приложения (с задержкой): '" + errors.get(0).getText().trim() + "'.");
            }
            throw new AssertionError("Подарок '" + name + "' не появился на странице в течение 10 секунд.");
        }
    }
}