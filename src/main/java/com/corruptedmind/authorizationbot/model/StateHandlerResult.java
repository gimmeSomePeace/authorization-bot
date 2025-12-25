package com.corruptedmind.authorizationbot.model;

import com.corruptedmind.authorizationbot.state.UserState;

import java.util.Optional;

public record StateHandlerResult(UserResponse userResponse, UserState nextState) {
    public Optional<UserState> nextStateOpt() {
        return Optional.ofNullable(nextState);
    }
}
