package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateHandler;

import java.util.function.Consumer;


/**
 * Состояние по умолчанию для новых пользователй. Начинает диалог с пользователем
 */
public class IdleStateHandler implements UserStateHandler {
    @Override
    public UserResponse handle(UserRequest userRequest, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated) {
        // предлагаем пользователю выбрать сервис для аутентификации
        UserResponse userResponse = new UserResponse(
                userRequest.userId(),
                "Здравствуйте! Выберите сервис, через который хотите авторизоваться (введите цифру от 1 до 1):\n" +
                        "    1) GitHub"
        );
        // изменяем активное состояние пользователя
        onUserInfoUpdated.accept(
                userInfo.builder().state(UserState.LOGIN).build()
        );
        return userResponse;
    }
}
