package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateManager;

public class AuthorizationLogicCore implements LogicCore {
    private final UserStateManager userStateManager;

    public AuthorizationLogicCore() {
        userStateManager = new UserStateManager();
    }


    @Override
    public UserResponse handle(UserRequest userRequest) {
        UserState activeState = userStateManager.getActiveState(userRequest.userId());
        UserResponse userResponse = activeState.handle(userRequest, userStateManager);
        return userResponse;
    }
}