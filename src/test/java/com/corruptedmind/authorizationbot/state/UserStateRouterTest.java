package com.corruptedmind.authorizationbot.state;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.EnumMap;

public class UserStateRouterTest {
    private EnumMap<UserState, UserStateHandler> fullMap() {
        EnumMap<UserState, UserStateHandler> map = new EnumMap<>(UserState.class);
        for (UserState state: UserState.values()) {
            map.put(state, mock(UserStateHandler.class));
        }
        return map;
    }

    @Test
    void shouldCreateRouterWhenAllStatesPresent() {
        EnumMap<UserState, UserStateHandler> map = fullMap();
        assertDoesNotThrow(() -> new UserStateRouter(map));
    }

    @ParameterizedTest
    @EnumSource(UserState.class)
    void shouldFailIfAnyStateMissing(UserState state) {
        EnumMap<UserState, UserStateHandler> map = fullMap();
        map.remove(state);

        assertThrows(IllegalStateException.class, () -> new UserStateRouter(map));
    }

    @ParameterizedTest
    @EnumSource(UserState.class)
    void shouldReturnHandlerForEveryState(UserState state) {
        EnumMap<UserState, UserStateHandler> map = fullMap();
        UserStateRouter router = new UserStateRouter(map);
        assertNotNull(router.getHandler(state));
    }

    @Test
    void shouldFailOnNullMap() {
        assertThrows(NullPointerException.class, () -> new UserStateRouter(null));
    }
}
