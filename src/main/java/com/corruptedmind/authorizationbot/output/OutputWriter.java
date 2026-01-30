package com.corruptedmind.authorizationbot.output;

import com.corruptedmind.authorizationbot.model.UserResponse;

/**
 * Интерфейс для вывода данных пользователю
 * Он абстрагирует процесс передачи ответа пользователю,
 * позволяя использовать разные способы вывода (консоль, API и тд).
 */
public interface OutputWriter {
    /**
     * Выводит сообщение пользователю
     * @param response сообщения для пользователя
     */
    void write(UserResponse response);
}