package com.corruptedmind.authorizationbot.core;

import com.corruptedmind.authorizationbot.model.UserId;
import com.corruptedmind.authorizationbot.model.UserInfoManager;
import com.corruptedmind.authorizationbot.model.UserRequest;
import com.corruptedmind.authorizationbot.state.UserState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OAuthDeviceFlowLogicCoreTest {

    @Test
    void handle_checkStateSwitching() {
        String testMessage = "test";
        UserId testUserId = new UserId("CONSOLE", "1");

        UserInfoManager userInfoManager = new UserInfoManager(UserState.IDLE);
        LogicCore logicCore = new OAuthDeviceFlowLogicCore(userInfoManager);
        // TODO: написать тесты
    }
}