package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Page Object для страницы регистрации.
 */
public class RegisterPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(RegisterPage.class);

    private final By nameInput = By.xpath("//label[text()='Имя пользователя']/following-sibling::input");
    private final By emailInput = By.xpath("//label[text()='Email']/following-sibling::input");
    private final By passInput = By.xpath("//label[text()='Пароль']/following-sibling::input");
    private final By submitBtn = By.xpath("//button[contains(text(), 'Зарегистрироваться')]");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Выполняет регистрацию нового пользователя.
     * @param name имя пользователя
     * @param email email
     * @param pass пароль
     */
    public void register(String name, String email, String pass) {
        logger.info("Регистрация пользователя: {}", name);
        type(nameInput, name);
        type(emailInput, email);
        type(passInput, pass);
        click(submitBtn);
        wait.until(ExpectedConditions.urlContains("/login"));
    }
}