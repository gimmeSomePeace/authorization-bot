package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.StateHandlerResult;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;
import com.corruptedmind.authorizationbot.state.UserState;
import com.corruptedmind.authorizationbot.state.UserStateManager;

import java.util.Optional;

public class AuthorizationLogicCore implements LogicCore {
    private final UserStateManager userStateManager;

    public AuthorizationLogicCore() {
        userStateManager = new UserStateManager();
    }

    @Override
    public UserResponse handle(UserRequest userRequest) {
        UserState activeState = userStateManager.getActiveState(userRequest.userId());
        StateHandlerResult result = activeState.handle(userRequest);
        Optional<UserState> nextState = result.nextStateOpt();
        nextState.ifPresent(state ->
                userStateManager.updateActiveState(userRequest.userId(), state)
        );
        return result.userResponse();
    }
}