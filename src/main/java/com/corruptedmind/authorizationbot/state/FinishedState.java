package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.StateHandlerResult;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;

public class FinishedState implements UserStateHandler {
    @Override
    public StateHandlerResult handle(UserRequest userRequest) {
        UserResponse userResponse = new UserResponse(userRequest.userId(), "FINISHED state");
        StateHandlerResult result = new StateHandlerResult(userResponse, UserState.IDLE);
        return result;
    }
}
