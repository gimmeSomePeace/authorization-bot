package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationLogicCoreTest {

    @Test
    void handle_checkStateSwitching() {
        String testMessage = "test";
        String testUserId = "CONSOLE_1";

        AuthorizationLogicCore logicCore = new AuthorizationLogicCore();
        String result = logicCore.handle(new UserRequest(testUserId, testMessage)).text();
        assertEquals("IDLE state", result);

        result = logicCore.handle(new UserRequest(testUserId, testMessage)).text();
        assertEquals("FINISHED state", result);
    }
}