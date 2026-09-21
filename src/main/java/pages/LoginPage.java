package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Page Object для страницы входа.
 */
public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    private final By nameInput = By.xpath("//label[text()='Имя пользователя']/following-sibling::input");
    private final By passInput = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private final By submitBtn = By.xpath("//button[contains(text(), 'Войти')]");
    private final By errorMsg = By.className("alert-danger");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Выполняет вход в систему и ждет редиректа в личный кабинет.
     * @param name имя пользователя
     * @param pass пароль
     */
    public void login(String name, String pass) {
        logger.info("Вход под пользователем: {}", name);
        type(nameInput, name);
        type(passInput, pass);
        click(submitBtn);

        // Ждем, пока URL перестанет содержать "/login" (успешный редирект)
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }

    /**
     * Проверяет, что отображается сообщение об ошибке входа.
     * @return true если ошибка отображается
     */
    public boolean isErrorDisplayed() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg));
            return error.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}