# WishlistProject

Автоматизированные E2E тесты для приложения [Wishlist](https://wishlist.otus.kartushin.su/wishlists/).

## Стек технологий
- Java 17
- Maven
- JUnit 5
- Selenium WebDriver 4
- WebDriverManager
- Log4j2

## Покрытые функциональности
1. Регистрация пользователя
2. Авторизация пользователя
3. Создание списка желаний
4. Просмотр деталей списка
5. Добавление подарка в список

## Запуск тестов
```bash
mvn clean test
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox