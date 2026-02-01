package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.core.Action;
import com.corruptedmind.authorizationbot.core.Event;
import com.corruptedmind.authorizationbot.model.UserInfo;

import java.util.List;

/**
 * Обработчик состояния пользователя
 */
public interface UserStateHandler {
    /**
     * Обрабатывает запрос пользователя в рамках текущего состояния
     * @param event
     * @param userInfo текущая информация о пользователе
     * @return ответ пользователю
     */
    List<Action> handle(Event event, UserInfo userInfo);
}
