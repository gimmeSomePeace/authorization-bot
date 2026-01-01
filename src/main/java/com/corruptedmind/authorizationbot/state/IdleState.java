package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.StateHandlerResult;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;


public class IdleState implements UserStateHandler {
    @Override
    public StateHandlerResult handle(UserRequest userRequest) {
        UserResponse userResponse = new UserResponse(userRequest.userId(), "IDLE state");
        StateHandlerResult result = new StateHandlerResult(userResponse, UserState.LOGIN);
        return result;
    }
}
