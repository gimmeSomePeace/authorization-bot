package com.corruptedmind.authorizationbot.model;

import com.corruptedmind.authorizationbot.state.UserState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserInfoManagerTest {
    private UserInfoManager userInfoManager;
    private UserState defaultState;

    @BeforeEach
    void setUp() {
        defaultState = mock(UserState.class);
        userInfoManager = new UserInfoManager(defaultState);
    }

    @Test
    void testNewUserAppeared() {
        UserId userId = mock(UserId.class);
        UserInfo userInfo = userInfoManager.getUserInfo(userId);

        assertNotNull(userInfo);
        assertEquals(userInfo.id(), userId);
        assertNull(userInfo.login());
        assertNull(userInfo.token());
        assertEquals(userInfo.state(), defaultState);
    }
}
