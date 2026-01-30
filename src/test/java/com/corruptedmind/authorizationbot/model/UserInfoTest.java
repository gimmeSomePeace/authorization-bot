package com.corruptedmind.authorizationbot.model;

import com.corruptedmind.authorizationbot.oauth.dto.AccessToken;
import com.corruptedmind.authorizationbot.state.UserState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UserInfoTest {
    @Test
    void testUserInfoCreation() {
        UserId userId = mock(UserId.class);
        String login = "test-login";
        AccessToken accessToken = mock(AccessToken.class);
        UserState userState = mock(UserState.class);

        // Создание объекта через Builder
        UserInfo userInfo = new UserInfo.Builder()
                .id(userId)
                .login(login)
                .token(accessToken)
                .state(userState)
                .build();

        // Проверяем, что все параметры установлены правильно
        assertEquals(userId, userInfo.id());
        assertEquals(login, userInfo.login());
        assertEquals(accessToken, userInfo.token());
        assertEquals(userState, userInfo.state());
    }

    @Test
    void testUserInfoBuilderDefaultValues() {
        UserId userId = mock(UserId.class);
        String login = "test-login";

        UserInfo userInfo = new UserInfo.Builder()
                .id(userId)
                .login(login)
                .build();

        // Проверяем, что все обязательные параметры установлены
        assertEquals(userId, userInfo.id());
        assertEquals(login, userInfo.login());
        // Остальные - null
        assertNull(userInfo.token());
        assertNull(userInfo.state());
    }
}
