package com.corruptedmind.authorizationbot.model;

/**
 * Контейнер для хранения id пользователей с разных платформ.
 * Хранится префикс платформы и id пользователя на этой платформе.
 * Сделано это с той целью, что на разных платформах у разных пользователей id могут совпасть.
 * Полный id с префиксом используется как id в этом приложении
 * @param platformPrefix префикс платформы (например, "vk", "tg")
 * @param platformUserId идентификатор пользователя на платформе
 */
public record UserId(String platformPrefix, String platformUserId) {
    public static final String separator = "_";

    /**
     * Возвращает полный идентификатор пользователя с префиксом платформы
     */
    public String getFullUserId() {
        return platformPrefix + separator + platformUserId;
    }
}
