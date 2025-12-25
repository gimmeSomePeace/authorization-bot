package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.StateHandlerResult;
import com.corruptedmind.authorizationbot.model.UserRequest;

public enum UserState {
    IDLE(new IdleState()),
    FINISHED(new FinishedState());

    private final UserStateHandler userStateHandler;

    UserState(UserStateHandler userStateHandler) {
        this.userStateHandler = userStateHandler;
    }

    public StateHandlerResult handle(UserRequest userRequest) {
        return userStateHandler.handle(userRequest);
    }
}
