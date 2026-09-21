package factory;

import enums.Browser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Фабрика для создания экземпляров WebDriver.
 */
public class WebDriverFactory {

    /**
     * Создает WebDriver без дополнительных опций.
     * @param browserName имя браузера
     * @return настроенный WebDriver
     */
    public static WebDriver createNewDriver(String browserName) {
        return createNewDriver(browserName, null);
    }

    /**
     * Создает WebDriver с опциональными настройками.
     * @param browserName имя браузера
     * @param options опции браузера (ChromeOptions, FirefoxOptions и т.д.)
     * @return настроенный WebDriver
     */
    public static WebDriver createNewDriver(String browserName, Object options) {
        Browser browser = Browser.fromString(browserName);
        WebDriver driver;

        switch (browser) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                driver = (options != null) ? new ChromeDriver((ChromeOptions) options) : new ChromeDriver();
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                driver = (options != null) ? new FirefoxDriver((FirefoxOptions) options) : new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserName);
        }
        driver.manage().window().maximize();
        return driver;
    }
}