package com.corruptedmind.authorizationbot.state;

import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;

public enum UserState {
    IDLE {
        @Override
        public UserResponse handle(UserRequest userRequest, UserStateManager userStateManager) {
            UserResponse userResponse = new UserResponse(userRequest.userId(), "IDLE state");
            userStateManager.updateActiveState(userRequest.userId(), FINISHED);
            return userResponse;
        }
    },
    FINISHED {
        @Override
        public UserResponse handle(UserRequest userRequest, UserStateManager userStateManager) {
            UserResponse userResponse = new UserResponse(userRequest.userId(), "FINISHED state");
            userStateManager.updateActiveState(userRequest.userId(), IDLE);
            return userResponse;
        }
    };
    public abstract UserResponse handle(UserRequest userRequest, UserStateManager userStateManager);
}
