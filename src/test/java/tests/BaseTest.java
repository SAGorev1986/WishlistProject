package tests;

import factory.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Базовый класс для всех тестов.
 */
public class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        String browser = System.getProperty("browser", "chrome");
        ChromeOptions options = new ChromeOptions();
        driver = WebDriverFactory.createNewDriver(browser, options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}