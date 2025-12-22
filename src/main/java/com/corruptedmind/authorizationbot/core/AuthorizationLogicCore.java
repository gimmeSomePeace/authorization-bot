package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;

public class AuthorizationLogicCore implements LogicCore {


    @Override
    public UserResponse handle(UserRequest userRequest) {
        return new UserResponse(userRequest.text());
    }
}