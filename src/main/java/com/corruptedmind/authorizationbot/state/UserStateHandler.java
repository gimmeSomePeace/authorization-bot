package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;

import java.util.function.Consumer;

/**
 * Обработчик состояния пользователя
 */
public interface UserStateHandler {
    /**
     * Обрабатывает запрос пользователя в рамках текущего состояния
     * @param userRequest запрос пользователя
     * @param userInfo текущая информация о пользователе
     * @param onUserInfoUpdated callback, вызываемый при обновлении информации о пользователе
     * @return ответ пользователю
     */
    UserResponse handle(UserRequest userRequest, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated);
}
