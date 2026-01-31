package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateHandler;

import java.util.function.Consumer;

public class FinishedStateHandler implements UserStateHandler {
    @Override
    public UserResponse handle(UserRequest userRequest, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated) {
        if (userRequest.text().equals("q")) {
            onUserInfoUpdated.accept(new UserInfo(userInfo.id(), null, null, UserState.IDLE));
            return new UserResponse(userInfo.id(), "Вы успешно вышли из аккаунта");
        }
       return new UserResponse(
               userRequest.userId(),
               String.format("Здравствуйте, %s! Вы авторизованы в системе. Для выхода из аккаунта введите q", userInfo.login())
        );
    }
}
