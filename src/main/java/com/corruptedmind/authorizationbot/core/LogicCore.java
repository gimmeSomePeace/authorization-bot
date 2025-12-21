package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.model.UserResponse;

public interface LogicCore {
    UserResponse handle(UserRequest userRequest);
}