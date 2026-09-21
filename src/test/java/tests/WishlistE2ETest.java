package tests;

import org.junit.jupiter.api.*;
import pages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * E2E тесты для приложения Wishlist.
 */
public class WishlistE2ETest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(WishlistE2ETest.class);
    private static final String BASE_URL = System.getProperty("test.url", "https://wishlist.otus.kartushin.su");

    private RegisterPage registerPage;
    private LoginPage loginPage;
    private WishlistsPage wishlistsPage;
    private WishlistDetailsPage detailsPage;

    @BeforeEach
    public void initPages() {
        registerPage = new RegisterPage(driver);
        loginPage = new LoginPage(driver);
        wishlistsPage = new WishlistsPage(driver);
        detailsPage = new WishlistDetailsPage(driver);
    }

    @Test
    public void testUserRegistration() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 5);
        String user = "RegUser_" + uniqueId;

        driver.get(BASE_URL + "/register");
        registerPage.register(user, user + "@test.com", "Password123!");

        assertTrue(driver.getCurrentUrl().contains("/login"), "Должен быть редирект на логин");
        logger.info("✅ Тест 1 пройден: Регистрация успешна");
    }

    @Test
    public void testSuccessfulLogin() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 5);
        String user = "AuthUser_" + uniqueId;
        String pass = "Password123!";

        driver.get(BASE_URL + "/register");
        registerPage.register(user, user + "@test.com", pass);

        loginPage.login(user, pass);

        assertFalse(driver.getCurrentUrl().contains("/register"), "Не должны быть на регистрации");
        logger.info("✅ Тест 2 пройден: Авторизация успешна");
    }

    @Test
    public void testCreateWishlist() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 5);
        String user = "ListUser_" + uniqueId;
        String listTitle = "Мой список " + uniqueId;

        driver.get(BASE_URL + "/register");
        registerPage.register(user, user + "@test.com", "Password123!");
        loginPage.login(user, "Password123!");

        wishlistsPage.createNewList(listTitle, "Тестовое описание");

        assertTrue(driver.getPageSource().contains(listTitle), "Список должен быть создан и отображаться на странице");
        logger.info("✅ Тест 3 пройден: Список создан");
    }

    @Test
    public void testViewWishlistDetails() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 5);
        String user = "ViewUser_" + uniqueId;
        String listTitle = "Список для просмотра " + uniqueId;

        driver.get(BASE_URL + "/register");
        registerPage.register(user, user + "@test.com", "Password123!");
        loginPage.login(user, "Password123!");
        wishlistsPage.createNewList(listTitle, "Описание");

        wishlistsPage.openWishlistDetails(listTitle);

        assertTrue(driver.getPageSource().contains(listTitle), "Должны быть на странице деталей списка");
        logger.info("✅ Тест 4 пройден: Детали списка открыты");
    }

    @Test
    public void testAddGiftToWishlist() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 5);
        String user = "GiftUser_" + uniqueId;
        String listTitle = "Список для подарка " + uniqueId;
        String giftName = "Подарок " + uniqueId;

        driver.get(BASE_URL + "/register");
        registerPage.register(user, user + "@test.com", "Password123!");
        loginPage.login(user, "Password123!");
        wishlistsPage.createNewList(listTitle, "Описание");
        wishlistsPage.openWishlistDetails(listTitle);

        // Метод addGift теперь сам ждет появления подарка на странице
        detailsPage.addGift(giftName, "Описание подарка");

        // Финальная перестраховка (теперь она гарантированно сработает)
        assertTrue(driver.getPageSource().contains(giftName), "Название подарка должно присутствовать на странице");
        logger.info("✅ Тест 5 пройден: Подарок добавлен");
    }
}