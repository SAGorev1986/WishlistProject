package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Таймаут 10 секунд, опрос каждые 500мс
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofMillis(500));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Умная обертка ожидания. Ждет появления элемента, но если падает по таймауту -
     * проверяет, не появилось ли на странице специфичной ошибки приложения.
     */
    protected void waitForElementOrCatchAppError(By successLocator, By appErrorLocator, String successLogMessage) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successLocator));
            logger.info(successLogMessage);
        } catch (TimeoutException e) {
            // Если таймаут, проверяем, не вылезла ли ошибка приложения (один раз, без лишних проверок)
            if (appErrorLocator != null) {
                List<WebElement> errors = driver.findElements(appErrorLocator);
                if (!errors.isEmpty() && errors.get(0).isDisplayed()) {
                    throw new AssertionError("Сбой на стороне приложения: '" + errors.get(0).getText().trim() + "'");
                }
            }
            throw new AssertionError("Элемент не появился на странице в течение 10 секунд: " + successLocator.toString());
        }
    }
}