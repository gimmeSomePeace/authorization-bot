package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;

import java.util.function.Consumer;


public class IdleState implements UserStateHandler {
    @Override
    public UserResponse handle(UserRequest userRequest, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated) {
        UserResponse userResponse = new UserResponse(
                userRequest.userId(),
                "Здравствуйте! Выберите сервис, через который хотите авторизоваться (введите цифру от 1 до 1):\n" +
                        "    1) GitHub"
        );
        onUserInfoUpdated.accept(
                userInfo.builder().state(UserState.LOGIN).build()
        );
        return userResponse;
    }
}
