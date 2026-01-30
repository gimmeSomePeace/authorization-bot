package com.corruptedmind.authorizationbot.model;

/**
 * Ответ для пользователя
 * @param userId id пользователя, которому необходимо отправить ответ
 * @param text содержание ответа
 */
public record UserResponse(UserId userId, String text) {
}
