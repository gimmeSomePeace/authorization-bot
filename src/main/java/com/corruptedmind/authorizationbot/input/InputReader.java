package com.corruptedmind.authorizationbot.input;

import com.corruptedmind.authorizationbot.model.UserRequest;


public interface InputReader {
    UserRequest read();
}
