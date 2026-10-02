package factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverFactory {

    public static WebDriver createNewDriver(String browserName) {
        if (browserName == null) {
            browserName = System.getProperty("browser", "chrome");
        }

        switch (browserName.toLowerCase()) {
            case "firefox":
                FirefoxOptions ffOptions = new FirefoxOptions();
                ffOptions.addArguments("--start-maximized");
                if (Boolean.getBoolean("headless")) ffOptions.addArguments("-headless");
                return WebDriverManager.firefoxdriver().capabilities(ffOptions).create();

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                if (Boolean.getBoolean("headless")) edgeOptions.addArguments("--headless");
                return WebDriverManager.edgedriver().capabilities(edgeOptions).create();

            default: // chrome
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                if (Boolean.getBoolean("headless")) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                return WebDriverManager.chromedriver().capabilities(chromeOptions).create();
        }
    }
}