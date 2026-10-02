package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class WishlistDetailsPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(WishlistDetailsPage.class);

    private final By addGiftBtn = By.xpath("//button[contains(., 'Добавить подарок')]");
    private final By giftModal = By.xpath("//div[contains(@class, 'modal-title') and contains(., 'Добавить подарок')]/ancestor::div[contains(@class, 'modal-content')]");

    private final By giftNameInput = By.xpath("//label[text()='Название']/following-sibling::input");
    private final By giftDescInput = By.xpath("//label[text()='Описание']/following-sibling::textarea");
    private final By storeLinkInput = By.xpath("//label[contains(., 'Ссылка на магазин')]/following-sibling::input");
    private final By priceInput = By.xpath("//label[contains(., 'Цена')]/following-sibling::input");
    private final By imageLinkInput = By.xpath("//label[contains(., 'Ссылка на изображение')]/following-sibling::input");
    private final By giftSubmitBtn = By.xpath("//button[@type='submit' and contains(., 'Добавить')]");

    public WishlistDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void addGift(String name, String description) {
        logger.info("Добавление подарка: {}", name);
        click(addGiftBtn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(giftModal));

        type(giftNameInput, name);
        type(giftDescInput, description);
        type(storeLinkInput, "https://example.com/product");
        type(priceInput, "100");
        type(imageLinkInput, "https://example.com/image.jpg");

        click(giftSubmitBtn);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(giftModal));

        By errorLocator = By.xpath("//*[contains(., 'Не удалось добавить подарок')]");
        By successLocator = By.xpath("//*[contains(., '" + name + "')]");

        List<WebElement> errors = driver.findElements(errorLocator);
        if (!errors.isEmpty() && errors.get(0).isDisplayed()) {
            throw new AssertionError("Сбой на стороне приложения: '" + errors.get(0).getText().trim() + "'.");
        }

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successLocator));
            logger.info("Подарок '{}' успешно добавлен", name);
        } catch (TimeoutException e) {
            throw new AssertionError("Подарок '" + name + "' не появился на странице в течение 10 секунд.");
        }
    }

    // Метод-ассерт для подарка
    public void assertGiftExists(String giftName) {
        if (!driver.getPageSource().contains(giftName)) {
            throw new AssertionError("Подарок '" + giftName + "' должен присутствовать на странице");
        }
    }
}