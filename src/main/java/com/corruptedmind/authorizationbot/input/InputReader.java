package com.corruptedmind.authorizationbot.input;

import com.corruptedmind.authorizationbot.core.Event.UserMessageEvent;
import com.corruptedmind.authorizationbot.model.UserRequest;

/**
 * Интерфейс для чтения данных от пользователя
 * Он абстрагирует процесс получения пользовательского ввода,
 * позволяя использовать разные источники данных (консоль, API и тд).
 */
public interface InputReader {
    /**
     * Читает входные данные и возвращает по одному сообщению
     * @return объект {@link UserRequest}, абстрагирующий пользовательское сообщение
     */
    UserRequest read();
}
