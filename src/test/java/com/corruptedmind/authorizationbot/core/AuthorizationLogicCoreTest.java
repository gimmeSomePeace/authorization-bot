package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationLogicCoreTest {

    @Test
    void handle_returnsEcho() {
        String testMessage = "test";

        AuthorizationLogicCore logicCore = new AuthorizationLogicCore();
        String result = logicCore.handle(new UserRequest(testMessage)).text();
        assertEquals("now", result);
    }
}