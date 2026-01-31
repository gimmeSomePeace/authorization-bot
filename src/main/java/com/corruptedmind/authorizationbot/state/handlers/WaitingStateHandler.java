package com.corruptedmind.authorizationbot.state.handlers;

import com.corruptedmind.authorizationbot.model.UserInfo;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.state.UserStateHandler;

import java.util.function.Consumer;

public class WaitingStateHandler implements UserStateHandler {
    @Override
    public UserResponse handle(UserRequest userRequest, UserInfo userInfo, Consumer<UserInfo> onUserInfoUpdated) {
         return new UserResponse(
                 userInfo.id(),
                 "Для продолжения работы приложения завершите авторизацию на сервисе"
         );
    }
}
