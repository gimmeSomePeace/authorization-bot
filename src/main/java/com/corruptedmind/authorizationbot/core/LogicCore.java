package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;

/**
 * Основа приложения. Обрабатывает запросы от пользователя и формирует на их основе ответ.
 */
public interface LogicCore {
    UserResponse handle(UserRequest userRequest);
}