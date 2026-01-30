package com.corruptedmind.authorizationbot.model;

/**
 * Класс, представляющий запрос пользователя.
 * Он может содержать различные данные, такие как текст сообщения, команда и тд
 * @param userId уникальный идентификатор пользователя
 * @param text данные
 */
public record UserRequest(UserId userId, String text) {
}
