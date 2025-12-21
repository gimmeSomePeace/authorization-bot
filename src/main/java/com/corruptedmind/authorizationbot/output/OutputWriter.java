package com.corruptedmind.authorizationbot.output;

import com.corruptedmind.authorizationbot.model.UserResponse;

public interface OutputWriter {
    void write(UserResponse response);
}