package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;

import java.util.function.Consumer;

/**
 * Описывает стадию диалога с пользователем. Каждая запись хранит в себе класс-обработчик состояния
 */
public enum UserState {
    IDLE(new IdleState()),
    LOGIN(new LoginState()),
    WAITING(new WaitingState()),
    FINISHED(new FinishedState());

    // класс-обработчик
    private final UserStateHandler userStateHandler;

    UserState(UserStateHandler userStateHandler) {
        this.userStateHandler = userStateHandler;
    }

    /**
     * Делегирует обработку пользовательского запроса {@link UserStateHandler}
     * @param userRequest входящий запрос пользователя
     * @param userInfo текущая информация о пользователе
     * @param onUserInfoUpdated callback, вызываемый при обновлении {@link UserInfo}
     * @return ответ пользователю
     */
    public UserResponse handle(UserRequest userRequest, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated) {
        return userStateHandler.handle(userRequest, userInfo, onUserInfoUpdated);
    }
}
