package enums;

/**
 * Enum для хранения поддерживаемых браузеров.
 */
public enum Browser {
    CHROME, FIREFOX, EDGE;

    /**
     * Преобразует строку в Browser, игнорируя регистр.
     * @param name имя браузера
     * @return соответствующий Browser или CHROME по умолчанию
     */
    public static Browser fromString(String name) {
        if (name == null || name.trim().isEmpty()) return CHROME;
        return valueOf(name.trim().toUpperCase());
    }
}